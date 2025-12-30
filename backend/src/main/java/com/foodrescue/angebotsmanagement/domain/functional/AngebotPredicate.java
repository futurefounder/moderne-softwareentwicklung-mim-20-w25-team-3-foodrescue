package com.foodrescue.angebotsmanagement.domain.functional;

import com.foodrescue.angebotsmanagement.domain.model.Angebot;

/**
 * Funktionales Interface für Angebot-Filter mit kombinierbaren Predicates.
 *
 * <p>Ermöglicht funktionale Komposition von Filter-Kriterien durch and/or-Operatoren.
 *
 * <p><strong>Funktionale Programmierkonzepte:</strong>
 *
 * <ul>
 *   <li>@FunctionalInterface: Single Abstract Method (SAM) für Lambda-Kompatibilität
 *   <li>Higher-Order Functions: and/or nehmen andere Predicates als Parameter
 *   <li>Function Composition: Predicates können beliebig kombiniert werden
 *   <li>Immutability: Jede Kombination erstellt ein neues Predicate
 * </ul>
 *
 * <p><strong>Usage Examples:</strong>
 *
 * <pre>{@code
 * // Einzelnes Predicate
 * AngebotPredicate istVerfuegbar = angebot -> angebot.getStatus() == Status.VERFUEGBAR;
 *
 * // Kombinierte Predicates
 * AngebotPredicate verfuegbarUndBio = istVerfuegbar()
 *     .and(hatTag("Bio"));
 *
 * AngebotPredicate komplexesSuche = istVerfuegbar()
 *     .and(hatAnbieter("123"))
 *     .and(verfuegbarAb(LocalDateTime.now()))
 *     .or(hatTag("Notfall"));
 * }</pre>
 */
@FunctionalInterface
public interface AngebotPredicate {

  /**
   * Testet ob ein Angebot die Bedingung erfüllt.
   *
   * @param angebot Das zu testende Angebot
   * @return true wenn Bedingung erfüllt, sonst false
   */
  boolean test(Angebot angebot);

  /**
   * Kombiniert dieses Predicate mit einem anderen durch logisches AND.
   *
   * <p>Funktionales Konzept: Function Composition
   *
   * @param other Das andere Predicate
   * @return Neues Predicate das beide Bedingungen prüft
   */
  default AngebotPredicate and(AngebotPredicate other) {
    return angebot -> this.test(angebot) && other.test(angebot);
  }

  /**
   * Kombiniert dieses Predicate mit einem anderen durch logisches OR.
   *
   * <p>Funktionales Konzept: Function Composition
   *
   * @param other Das andere Predicate
   * @return Neues Predicate das eine der Bedingungen prüft
   */
  default AngebotPredicate or(AngebotPredicate other) {
    return angebot -> this.test(angebot) || other.test(angebot);
  }

  /**
   * Negiert dieses Predicate.
   *
   * <p>Funktionales Konzept: Function Composition
   *
   * @return Neues Predicate mit umgekehrter Logik
   */
  default AngebotPredicate negate() {
    return angebot -> !this.test(angebot);
  }

  /**
   * Erstellt ein Predicate das immer true zurückgibt.
   *
   * <p>Nützlich als Startpunkt für dynamische Filter-Kombinationen.
   *
   * @return Always-true Predicate
   */
  static AngebotPredicate alwaysTrue() {
    return angebot -> true;
  }

  /**
   * Erstellt ein Predicate das immer false zurückgibt.
   *
   * @return Always-false Predicate
   */
  static AngebotPredicate alwaysFalse() {
    return angebot -> false;
  }
}
