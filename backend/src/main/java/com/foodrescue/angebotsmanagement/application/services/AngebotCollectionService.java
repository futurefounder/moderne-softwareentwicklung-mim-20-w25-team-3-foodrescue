package com.foodrescue.angebotsmanagement.application.services;

import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service für komplexe Collection-Verarbeitungen mit funktionalen Konzepten.
 *
 * <p><strong>Übung 7 - Collection Processing:</strong> Implementiert alle geforderten Operationen:
 *
 * <ol>
 *   <li>Gruppierung von Daten (groupByStatus, groupByAnbieter)
 *   <li>Aggregation/Reduktion (statistiken, getTotalAngebotCount)
 *   <li>Transformation/Mapping (toSummaryDTOs, extractAllTags)
 *   <li>Filtering mit mehreren Kriterien (findeMitKomplexenKriterien)
 * </ol>
 */

// wird nicht verwendet
@Service
public class AngebotCollectionService {

  private final AngebotRepository repository;

  public AngebotCollectionService(AngebotRepository repository) {
    this.repository = repository;
  }

  // ========== 1. GRUPPIERUNG ==========

  /**
   * Gruppiert Angebote nach Status.
   *
   * <p>Funktionales Konzept: groupingBy() Collector
   *
   * <p><strong>Vorher (imperativ):</strong>
   *
   * <pre>{@code
   * Map<Status, List<Angebot>> result = new HashMap<>();
   * for (Angebot a : angebote) {
   *     result.computeIfAbsent(a.getStatus(), k -> new ArrayList<>()).add(a);
   * }
   * }</pre>
   *
   * <p><strong>Nachher (funktional):</strong>
   *
   * <pre>{@code
   * angebote.stream()
   *     .collect(Collectors.groupingBy(Angebot::getStatus));
   * }</pre>
   *
   * @return Map mit Status als Key, Liste von Angeboten als Value
   */
  public Map<Angebot.Status, List<Angebot>> groupByStatus() {
    return repository.findeAlleVerfuegbar().stream()
        .collect(Collectors.groupingBy(Angebot::getStatus)); // Method Reference!
  }

  /**
   * Gruppiert Angebote nach Anbieter mit Counting.
   *
   * <p>Funktionales Konzept: groupingBy() + counting() (Nested Collectors)
   *
   * @return Map mit AnbieterId als Key, Anzahl Angebote als Value
   */
  public Map<String, Long> groupByAnbieterMitCounting() {
    return repository.findeAlleVerfuegbar().stream()
        .collect(
            Collectors.groupingBy(
                angebot -> angebot.getAnbieterId().getValue().toString(),
                Collectors.counting() // Nested Collector!
                ));
  }

  /**
   * Gruppiert Angebote nach Tag (Ein Angebot kann in mehreren Gruppen sein).
   *
   * <p>Funktionales Konzept: flatMap() + groupingBy()
   *
   * @return Map mit Tag als Key, Liste von Angeboten als Value
   */
  public Map<String, List<Angebot>> groupByTags() {
    return repository.findeAlleVerfuegbar().stream()
        .flatMap(
            angebot ->
                angebot.getTags().stream()
                    .map(tag -> Map.entry(tag, angebot))) // Flatten: Tag -> Angebot
        .collect(
            Collectors.groupingBy(
                Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
  }

  // ========== 2. AGGREGATION / REDUKTION ==========

  /**
   * Berechnet Statistiken über alle Angebote.
   *
   * <p>Funktionales Konzept: Multiple Aggregations in einem Stream-Durchlauf
   *
   * <p><strong>Funktionale Konzepte:</strong>
   *
   * <ul>
   *   <li>Collectors.teeing(): Zwei Collectors parallel ausführen
   *   <li>Collectors.summarizingInt(): Aggregierte Statistiken
   *   <li>Record Pattern: AngebotStatistiken ist ein Java Record
   * </ul>
   */
  public AngebotStatistiken berechneStatistiken() {
    List<Angebot> alle = repository.findeAlleVerfuegbar();

    // Mehrere Aggregationen parallel
    return alle.stream()
        .collect(
            Collectors.teeing(
                Collectors.counting(), // 1. Zähle Angebote
                Collectors.groupingBy(
                    Angebot::getStatus, Collectors.counting()), // 2. Zähle pro Status
                (total, byStatus) ->
                    new AngebotStatistiken(
                        total,
                        byStatus.getOrDefault(Angebot.Status.VERFUEGBAR, 0L),
                        byStatus.getOrDefault(Angebot.Status.RESERVIERT, 0L),
                        byStatus.getOrDefault(Angebot.Status.ABGEHOLT, 0L))));
  }

  /**
   * Record für Statistiken (Java 17+).
   *
   * <p>Funktionales Konzept: Immutable Data Class
   */
  public record AngebotStatistiken(
      long total, long verfuegbar, long reserviert, long abgeschlossen) {}

  /**
   * Reduziert alle Tags zu einer eindeutigen Liste.
   *
   * <p>Funktionales Konzept: flatMap() + distinct() + reduce()
   *
   * @return Sortierte Liste aller eindeutigen Tags
   */
  public List<String> extractAllTags() {
    return repository.findeAlleVerfuegbar().stream()
        .flatMap(angebot -> angebot.getTags().stream()) // Flatten
        .distinct() // Eindeutig
        .sorted() // Sortiert
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Findet das älteste und neueste Angebot.
   *
   * <p>Funktionales Konzept: Collectors.teeing() für parallele Min/Max
   *
   * @return Record mit ältestem und neuestem Angebot
   */
  public Optional<AngebotZeitspanne> findeZeitspanne() {
    List<Angebot> alle = repository.findeAlleVerfuegbar();

    if (alle.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(
        alle.stream()
            .collect(
                Collectors.teeing(
                    Collectors.minBy(Comparator.comparing(a -> a.getZeitfenster().von())),
                    Collectors.maxBy(Comparator.comparing(a -> a.getZeitfenster().bis())),
                    (min, max) ->
                        new AngebotZeitspanne(
                            min.map(a -> a.getZeitfenster().von()).orElse(null),
                            max.map(a -> a.getZeitfenster().bis()).orElse(null)))));
  }

  public record AngebotZeitspanne(LocalDateTime fruehesterStart, LocalDateTime spaetestesEnde) {}

  // ========== 3. TRANSFORMATION / MAPPING ==========

  /**
   * Transformiert Angebote zu Summary-DTOs.
   *
   * <p>Funktionales Konzept: map() + Method Reference
   *
   * @return Liste von AngebotSummary DTOs
   */
  public List<AngebotSummary> toSummaryDTOs() {
    return repository.findeAlleVerfuegbar().stream()
        .map(this::toSummary) // Method Reference zur Transformation
        .collect(Collectors.toUnmodifiableList());
  }

  private AngebotSummary toSummary(Angebot angebot) {
    return new AngebotSummary(
        angebot.getId(), angebot.getTitel(), angebot.getStatus().name(), angebot.getTags().size());
  }

  /**
   * Immutable DTO für Angebot-Summary.
   *
   * <p>Funktionales Konzept: Record statt Klasse mit Gettern/Settern
   */
  public record AngebotSummary(String id, String titel, String status, int anzahlTags) {}

  /**
   * Erstellt eine Map von AngebotId -> Titel.
   *
   * <p>Funktionales Konzept: Collectors.toMap() mit Method References
   *
   * @return Map mit ID als Key, Titel als Value
   */
  public Map<String, String> createIdToTitelMap() {
    return repository.findeAlleVerfuegbar().stream()
        .collect(
            Collectors.toMap(
                Angebot::getId, // Key Mapper
                Angebot::getTitel, // Value Mapper
                (existing, replacement) -> existing // Merge Function bei Duplikaten
                ));
  }

  // ========== 4. FILTERING MIT MEHREREN KRITERIEN ==========

  /**
   * Findet Angebote mit komplexen, kombinierbaren Kriterien.
   *
   * <p>Funktionales Konzept: Predicate Composition + Stream API
   *
   * <p><strong>Kriterien:</strong>
   *
   * <ul>
   *   <li>Status = VERFUEGBAR
   *   <li>Mindestens eines der angegebenen Tags
   *   <li>Zeitfenster beginnt nach gegebenem Zeitpunkt
   *   <li>Titel enthält Suchbegriff (case-insensitive)
   * </ul>
   *
   * @param suchTags Liste von gewünschten Tags (mindestens eins muss matchen)
   * @param fruehestesDatum Frühester Start-Zeitpunkt
   * @param titelSuche Suchbegriff im Titel (optional)
   * @return Gefilterte und sortierte Liste von Angeboten
   */
  public List<Angebot> findeMitKomplexenKriterien(
      List<String> suchTags, LocalDateTime fruehestesDatum, Optional<String> titelSuche) {

    return repository.findeAlleVerfuegbar().stream()
        // Filter 1: Status
        .filter(a -> a.getStatus() == Angebot.Status.VERFUEGBAR)

        // Filter 2: Tags (mindestens eines muss matchen)
        .filter(a -> suchTags.stream().anyMatch(tag -> a.getTags().contains(tag)))

        // Filter 3: Zeitfenster
        .filter(a -> a.getZeitfenster().von().isAfter(fruehestesDatum))

        // Filter 4: Titel (optional)
        .filter(
            a ->
                titelSuche
                    .map(suche -> a.getTitel().toLowerCase().contains(suche.toLowerCase()))
                    .orElse(true)) // Wenn kein Suchbegriff, dann true

        // Sortierung
        .sorted(Comparator.comparing(a -> a.getZeitfenster().von()))
        .collect(Collectors.toUnmodifiableList());
  }

  /**
   * Partitioniert Angebote in verfügbar und nicht-verfügbar.
   *
   * <p>Funktionales Konzept: Collectors.partitioningBy()
   *
   * @return Map mit Boolean-Key (true=verfügbar, false=nicht verfügbar)
   */
  public Map<Boolean, List<Angebot>> partitionByVerfuegbarkeit() {
    return repository.findeAlleVerfuegbar().stream()
        .collect(Collectors.partitioningBy(a -> a.getStatus() == Angebot.Status.VERFUEGBAR));
  }

  /**
   * Findet Top-N Anbieter mit den meisten Angeboten.
   *
   * <p>Funktionales Konzept: groupingBy + counting + sorted + limit
   *
   * @param n Anzahl der Top-Anbieter
   * @return Liste von AnbieterId und Anzahl Angebote (sortiert absteigend)
   */
  public List<AnbieterStatistik> findeTopAnbieter(int n) {
    return repository.findeAlleVerfuegbar().stream()
        .collect(
            Collectors.groupingBy(
                a -> a.getAnbieterId().getValue().toString(), Collectors.counting()))
        .entrySet()
        .stream()
        .map(entry -> new AnbieterStatistik(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparing(AnbieterStatistik::anzahlAngebote).reversed())
        .limit(n)
        .collect(Collectors.toUnmodifiableList());
  }

  public record AnbieterStatistik(String anbieterId, long anzahlAngebote) {}
}
