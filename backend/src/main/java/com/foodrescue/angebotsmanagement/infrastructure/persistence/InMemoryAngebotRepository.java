package com.foodrescue.angebotsmanagement.infrastructure.persistence;

import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import com.foodrescue.shared.domain.DomainEvent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

/**
 * In-Memory Implementierung des AngebotRepository.
 *
 * <p>Speichert Angebote in einer ConcurrentHashMap Publiziert Domain Events nach dem Speichern
 * Cleared Domain Events nach dem Publishing
 *
 * <p>In Produktion würde dies durch eine JPA-Implementierung ersetzt werden.
 */
@Repository
public class InMemoryAngebotRepository implements AngebotRepository {

  private final Map<String, Angebot> angebote = new ConcurrentHashMap<>();
  private final ApplicationEventPublisher eventPublisher;

  public InMemoryAngebotRepository(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  /**
   * Speichert ein Angebot und publiziert alle Domain Events.
   *
   * @param angebot Das zu speichernde Angebot
   * @return Das gespeicherte Angebot
   */
  @Override
  public Angebot speichern(Angebot angebot) {
    Objects.requireNonNull(angebot, "Angebot darf nicht null sein");

    String id = angebot.getId();

    // 1. Speichern
    angebote.put(id, angebot);

    // 2. Domain Events publizieren
    List<DomainEvent> events = angebot.getDomainEvents();
    if (!events.isEmpty()) {
      events.forEach(
          event -> {
            eventPublisher.publishEvent(event);
          });
    }

    // 3. Events aus Aggregate clearen (wichtig um Duplicate Publishing zu vermeiden)
    angebot.clearDomainEvents();

    return angebot;
  }

  @Override
  public Optional<Angebot> findeMitId(AngebotsId id) {
    Objects.requireNonNull(id, "AngebotsId darf nicht null sein");
    return Optional.ofNullable(angebote.get(id.value()));
  }

  @Override
  public List<Angebot> findeAlleVerfuegbar() {
    return angebote.values().stream()
        .filter(a -> a.getStatus() == Angebot.Status.VERFUEGBAR)
        .collect(Collectors.toList());
  }

  @Override
  public List<Angebot> findeFuerAnbieter(String anbieterId) {
    Objects.requireNonNull(anbieterId, "AnbieterId darf nicht null sein");

    return angebote.values().stream()
        .filter(a -> a.getAnbieterId().getValue().toString().equals(anbieterId))
        .collect(Collectors.toList());
  }

  /** Utility-Methode zum Löschen aller Angebote. Nur für Tests gedacht. */
  public void deleteAll() {
    angebote.clear();
  }

  /** Utility-Methode zum Abrufen der Anzahl gespeicherter Angebote. Nur für Tests gedacht. */
  public long count() {
    return angebote.size();
  }
}
