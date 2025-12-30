package com.foodrescue.angebotsmanagement.infrastructure.persistence;

import static com.foodrescue.angebotsmanagement.domain.functional.AngebotPredicates.*;

import com.foodrescue.angebotsmanagement.domain.functional.AngebotPredicate;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * Funktional refactored In-Memory Implementierung des AngebotRepository.
 *
 * <p><strong>Funktionale Verbesserungen gegenüber Original:</strong>
 *
 * <ul>
 *   <li>Event Publishing: Stream-basiert statt imperativer forEach-Loop
 *   <li>Filtering: Composable Predicates statt hardcoded Filter-Methoden
 *   <li>Method References: Verwendet ::publishEvent statt Lambdas
 *   <li>Immutable Collections: toUnmodifiableList() verhindert externe Modifikation
 *   <li>Optional: Explizites Null-Handling
 * </ul>
 */
@Repository
@Primary // Diese Bean wird bevorzugt
public class FunctionalAngebotRepository implements AngebotRepository {

  private static final Logger log = LoggerFactory.getLogger(FunctionalAngebotRepository.class);

  private final Map<String, Angebot> angebote = new ConcurrentHashMap<>();
  private final ApplicationEventPublisher eventPublisher;

  public FunctionalAngebotRepository(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  /**
   * VERBESSERUNG 1: Funktionales Event Publishing
   *
   * <p>Vorher (imperativ):
   *
   * <pre>{@code
   * List<DomainEvent> events = angebot.getDomainEvents();
   * if (!events.isEmpty()) {
   *     events.forEach(event -> {
   *         log.debug("Publishing Event: {}", event.getClass().getSimpleName());
   *         eventPublisher.publishEvent(event);
   *     });
   * }
   * }</pre>
   *
   * <p>Nachher (funktional):
   *
   * <pre>{@code
   * angebot.getDomainEvents().stream()
   *     .peek(event -> log.debug("Publishing Event: {}", event.getClass().getSimpleName()))
   *     .forEach(eventPublisher::publishEvent);
   * }</pre>
   *
   * <p>Verbesserungen:
   *
   * <ul>
   *   <li>Keine if-Prüfung nötig (empty stream wird ignoriert)
   *   <li>Method Reference statt Lambda: eventPublisher::publishEvent
   *   <li>Deklarativer Pipeline-Stil
   *   <li>3 Zeilen statt 8 Zeilen
   * </ul>
   */
  @Override
  public Angebot speichern(Angebot angebot) {
    Objects.requireNonNull(angebot, "Angebot darf nicht null sein");

    String id = angebot.getId();
    log.debug("Speichere Angebot mit ID: {}", id);

    // 1. Speichern
    angebote.put(id, angebot);

    // 2. Domain Events publizieren (FUNKTIONAL)
    angebot.getDomainEvents().stream()
        .peek(event -> log.debug("Publishing Event: {}", event.getClass().getSimpleName()))
        .forEach(eventPublisher::publishEvent); // Method Reference!

    // 3. Events clearen
    angebot.clearDomainEvents();

    return angebot;
  }

  @Override
  public Optional<Angebot> findeMitId(AngebotsId id) {
    Objects.requireNonNull(id, "AngebotsId darf nicht null sein");
    return Optional.ofNullable(angebote.get(id.value()));
  }

  /**
   * VERBESSERUNG 2: Generisches Filtering mit Predicates
   *
   * <p>Ersetzt hardcoded Methoden wie findeAlleVerfuegbar(), findeFuerAnbieter(), etc.
   *
   * <p>Funktionale Konzepte:
   *
   * <ul>
   *   <li>Higher-Order Function: Nimmt Predicate als Parameter
   *   <li>Composability: Predicates können kombiniert werden
   *   <li>Immutability: Returned unmodifiable List
   * </ul>
   *
   * <p>Usage:
   *
   * <pre>{@code
   * // Ersetzt findeAlleVerfuegbar()
   * repository.finde(istVerfuegbar());
   *
   * // Ersetzt findeFuerAnbieter(id)
   * repository.finde(hatAnbieter(id));
   *
   * // Neue Kombinationen möglich!
   * repository.finde(istVerfuegbar().and(hatTag("Bio")));
   * }</pre>
   */
  public List<Angebot> finde(AngebotPredicate predicate) {
    Objects.requireNonNull(predicate, "Predicate darf nicht null sein");

    return angebote.values().stream()
        .filter(predicate::test) // Method Reference!
        .collect(Collectors.toUnmodifiableList()); // Immutable!
  }

  /**
   * Legacy-Methoden für Kompatibilität mit Interface.
   *
   * <p>Implementiert mit neuer finde()-Methode + Predicates.
   */
  @Override
  public List<Angebot> findeAlleVerfuegbar() {
    return finde(istVerfuegbar());
  }

  @Override
  public List<Angebot> findeFuerAnbieter(String anbieterId) {
    Objects.requireNonNull(anbieterId, "AnbieterId darf nicht null sein");
    return finde(hatAnbieter(anbieterId));
  }

  // ========== Neue funktionale Query-Methoden ==========

  /**
   * Findet alle Angebote die ALLE angegebenen Predicates erfüllen.
   *
   * <p>Funktionales Konzept: AND-Kombination von Predicates
   *
   * @param predicates Variable Anzahl von Predicates
   * @return Liste von Angeboten die alle Bedingungen erfüllen
   */
  public List<Angebot> findeAlle(AngebotPredicate... predicates) {
    if (predicates.length == 0) {
      return List.copyOf(angebote.values());
    }

    // Reduce: Kombiniere alle Predicates mit AND
    AngebotPredicate combined =
        Arrays.stream(predicates).reduce(AngebotPredicate.alwaysTrue(), AngebotPredicate::and);

    return finde(combined);
  }

  /**
   * Findet Angebote und transformiert sie zu einem anderen Typ.
   *
   * <p>Funktionales Konzept: Map (Transformation)
   *
   * @param predicate Filter-Bedingung
   * @param mapper Transformationsfunktion
   * @param <R> Ziel-Typ
   * @return Liste von transformierten Objekten
   */
  public <R> List<R> findeUndMappiere(
      AngebotPredicate predicate, java.util.function.Function<Angebot, R> mapper) {
    Objects.requireNonNull(predicate, "Predicate darf nicht null sein");
    Objects.requireNonNull(mapper, "Mapper darf nicht null sein");

    return angebote.values().stream()
        .filter(predicate::test)
        .map(mapper) // Transform!
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Zählt Angebote die ein Predicate erfüllen.
   *
   * <p>Funktionales Konzept: Reduce (zu Long)
   *
   * @param predicate Filter-Bedingung
   * @return Anzahl der Angebote
   */
  public long zaehle(AngebotPredicate predicate) {
    Objects.requireNonNull(predicate, "Predicate darf nicht null sein");

    return angebote.values().stream().filter(predicate::test).count();
  }

  /**
   * Prüft ob mindestens ein Angebot das Predicate erfüllt.
   *
   * <p>Funktionales Konzept: Short-circuit Evaluation
   *
   * @param predicate Filter-Bedingung
   * @return true wenn mindestens ein Match existiert
   */
  public boolean existiert(AngebotPredicate predicate) {
    Objects.requireNonNull(predicate, "Predicate darf nicht null sein");

    return angebote.values().stream().anyMatch(predicate::test);
  }

  /**
   * Findet das erste Angebot das ein Predicate erfüllt.
   *
   * <p>Funktionales Konzept: Optional + Short-circuit
   *
   * @param predicate Filter-Bedingung
   * @return Optional mit erstem Match
   */
  public Optional<Angebot> findeErstes(AngebotPredicate predicate) {
    Objects.requireNonNull(predicate, "Predicate darf nicht null sein");

    return angebote.values().stream().filter(predicate::test).findFirst();
  }

  // ========== Utility Methods (für Tests) ==========

  public void deleteAll() {
    log.warn("Lösche alle Angebote aus dem Repository");
    angebote.clear();
  }

  public long count() {
    return angebote.size();
  }
}
