package com.foodrescue.angebotsmanagement.domain.functional;

import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Factory-Klasse für vordefinierte Angebot-Predicates.
 *
 * <p>Bietet wiederverwendbare Filter-Funktionen für häufige Angebot-Queries.
 *
 * <p><strong>Funktionale Programmierkonzepte:</strong>
 *
 * <ul>
 *   <li>Pure Functions: Alle Methoden sind stateless und side-effect-free
 *   <li>Method References: Predicates sind direkt als Method References verwendbar
 *   <li>Higher-Order Functions: Methoden returnen Funktionen (Predicates)
 *   <li>Currying: Parametrisierte Predicates wie hatAnbieter(id) sind teilweise angewendete
 *       Funktionen
 * </ul>
 *
 * <p><strong>Design Pattern:</strong> Factory + Strategy Pattern (funktional)
 *
 * <p><strong>Usage Examples:</strong>
 *
 * <pre>{@code
 * // Import static
 * import static AngebotPredicates.*;
 *
 * // Einzelne Predicates
 * repository.finde(istVerfuegbar());
 * repository.finde(hatAnbieter("123"));
 *
 * // Kombinierte Predicates
 * repository.finde(
 *     istVerfuegbar()
 *         .and(hatTag("Bio"))
 *         .and(verfuegbarAb(LocalDateTime.now()))
 * );
 *
 * // Komplexe Business Rules
 * AngebotPredicate premiumSuche = istVerfuegbar()
 *     .and(hatTag("Premium").or(hatTag("Bio")))
 *     .and(verfuegbarInZeitfenster(heute, morgen));
 * }</pre>
 */
public final class AngebotPredicates {

  // Utility class - private constructor
  private AngebotPredicates() {
    throw new UnsupportedOperationException("Utility class");
  }

  // ========== Status-basierte Predicates ==========

  /**
   * Predicate für verfügbare Angebote.
   *
   * <p>Funktionales Konzept: Pure Function (keine Side-Effects)
   *
   * @return Predicate das auf VERFUEGBAR-Status prüft
   */
  public static AngebotPredicate istVerfuegbar() {
    return angebot -> angebot.getStatus() == Angebot.Status.VERFUEGBAR;
  }

  /**
   * Predicate für Entwürfe.
   *
   * @return Predicate das auf ENTWURF-Status prüft
   */
  public static AngebotPredicate istEntwurf() {
    return angebot -> angebot.getStatus() == Angebot.Status.ENTWURF;
  }

  /**
   * Predicate für reservierte Angebote.
   *
   * @return Predicate das auf RESERVIERT-Status prüft
   */
  public static AngebotPredicate istReserviert() {
    return angebot -> angebot.getStatus() == Angebot.Status.RESERVIERT;
  }

  /**
   * Predicate für abgeschlossene Angebote.
   *
   * @return Predicate das auf ABGESCHLOSSEN-Status prüft
   */
  public static AngebotPredicate istAbgeholt() {
    return angebot -> angebot.getStatus() == Angebot.Status.ABGEHOLT;
  }

  // ========== Anbieter-basierte Predicates ==========

  /**
   * Predicate für Angebote eines bestimmten Anbieters.
   *
   * <p>Funktionales Konzept: Currying - Parametrisierte Funktion
   *
   * @param anbieterId Die Anbieter-ID
   * @return Predicate das auf Anbieter-ID prüft
   * @throws NullPointerException wenn anbieterId null ist
   */
  public static AngebotPredicate hatAnbieter(String anbieterId) {
    Objects.requireNonNull(anbieterId, "anbieterId darf nicht null sein");
    return angebot -> angebot.getAnbieterId().getValue().toString().equals(anbieterId);
  }

  // ========== Tag-basierte Predicates ==========

  /**
   * Predicate für Angebote mit einem bestimmten Tag.
   *
   * <p>Funktionales Konzept: Higher-Order Function
   *
   * @param tag Der zu suchende Tag
   * @return Predicate das auf Tag-Existenz prüft
   * @throws NullPointerException wenn tag null ist
   */
  public static AngebotPredicate hatTag(String tag) {
    Objects.requireNonNull(tag, "tag darf nicht null sein");
    return angebot -> angebot.getTags() != null && angebot.getTags().contains(tag);
  }

  /**
   * Predicate für Angebote mit ALLEN angegebenen Tags.
   *
   * <p>Funktionales Konzept: Function Composition (AND-Verknüpfung)
   *
   * @param tags Die erforderlichen Tags
   * @return Predicate das auf Existenz aller Tags prüft
   */
  public static AngebotPredicate hatAlleTags(String... tags) {
    Objects.requireNonNull(tags, "tags darf nicht null sein");
    return angebot ->
        angebot.getTags() != null && angebot.getTags().containsAll(java.util.Arrays.asList(tags));
  }

  /**
   * Predicate für Angebote mit MINDESTENS EINEM der angegebenen Tags.
   *
   * <p>Funktionales Konzept: Function Composition (OR-Verknüpfung)
   *
   * @param tags Die möglichen Tags
   * @return Predicate das auf Existenz mindestens eines Tags prüft
   */
  public static AngebotPredicate hatEinesVonTags(String... tags) {
    Objects.requireNonNull(tags, "tags darf nicht null sein");
    return angebot -> {
      if (angebot.getTags() == null) return false;
      return java.util.Arrays.stream(tags).anyMatch(tag -> angebot.getTags().contains(tag));
    };
  }

  // ========== Zeit-basierte Predicates ==========

  /**
   * Predicate für Angebote die ab einem Zeitpunkt verfügbar sind.
   *
   * <p>Funktionales Konzept: Pure Function mit Zeitvergleich
   *
   * @param zeitpunkt Der Referenzzeitpunkt
   * @return Predicate das auf Verfügbarkeit ab Zeitpunkt prüft
   * @throws NullPointerException wenn zeitpunkt null ist
   */
  public static AngebotPredicate verfuegbarAb(LocalDateTime zeitpunkt) {
    Objects.requireNonNull(zeitpunkt, "zeitpunkt darf nicht null sein");
    return angebot ->
        angebot.getZeitfenster() != null && !angebot.getZeitfenster().von().isBefore(zeitpunkt);
  }

  /**
   * Predicate für Angebote die bis zu einem Zeitpunkt verfügbar sind.
   *
   * @param zeitpunkt Der Referenzzeitpunkt
   * @return Predicate das auf Verfügbarkeit bis Zeitpunkt prüft
   * @throws NullPointerException wenn zeitpunkt null ist
   */
  public static AngebotPredicate verfuegbarBis(LocalDateTime zeitpunkt) {
    Objects.requireNonNull(zeitpunkt, "zeitpunkt darf nicht null sein");
    return angebot ->
        angebot.getZeitfenster() != null && !angebot.getZeitfenster().bis().isAfter(zeitpunkt);
  }

  /**
   * Predicate für Angebote in einem Zeitfenster.
   *
   * <p>Kombiniert verfuegbarAb und verfuegbarBis mit AND.
   *
   * @param von Start des Zeitfensters
   * @param bis Ende des Zeitfensters
   * @return Predicate das auf Verfügbarkeit im Zeitfenster prüft
   * @throws NullPointerException wenn von oder bis null ist
   */
  public static AngebotPredicate verfuegbarInZeitfenster(LocalDateTime von, LocalDateTime bis) {
    return verfuegbarAb(von).and(verfuegbarBis(bis));
  }

  /**
   * Predicate für Angebote die aktuell verfügbar sind.
   *
   * <p>Dynamische Zeitprüfung basierend auf aktuellem Zeitpunkt.
   *
   * @return Predicate das auf aktuelle Verfügbarkeit prüft
   */
  public static AngebotPredicate istAktuellVerfuegbar() {
    return angebot -> {
      LocalDateTime now = LocalDateTime.now();
      return angebot.getZeitfenster() != null
          && !angebot.getZeitfenster().von().isAfter(now)
          && !angebot.getZeitfenster().bis().isBefore(now);
    };
  }

  // ========== Titel-basierte Predicates ==========

  /**
   * Predicate für Angebote mit Titel-Match (case-insensitive).
   *
   * <p>Funktionales Konzept: String-Transformation + Predicate
   *
   * @param suchbegriff Der zu suchende Begriff im Titel
   * @return Predicate das auf Titel-Match prüft
   * @throws NullPointerException wenn suchbegriff null ist
   */
  public static AngebotPredicate titelEnthaelt(String suchbegriff) {
    Objects.requireNonNull(suchbegriff, "suchbegriff darf nicht null sein");
    String lower = suchbegriff.toLowerCase();
    return angebot ->
        angebot.getTitel() != null && angebot.getTitel().toLowerCase().contains(lower);
  }

  // ========== Kombinierte Business Rules ==========

  /**
   * Predicate für "suchbare" Angebote (verfügbar + aktuell im Zeitfenster).
   *
   * <p>Business Rule: Kombination aus Status und Zeit
   *
   * @return Predicate für suchbare Angebote
   */
  public static AngebotPredicate istSuchbar() {
    return istVerfuegbar().and(istAktuellVerfuegbar());
  }

  /**
   * Predicate für "abgelaufene" Angebote (Zeitfenster-Ende in Vergangenheit).
   *
   * @return Predicate für abgelaufene Angebote
   */
  public static AngebotPredicate istAbgelaufen() {
    return angebot ->
        angebot.getZeitfenster() != null
            && angebot.getZeitfenster().bis().isBefore(LocalDateTime.now());
  }
}
