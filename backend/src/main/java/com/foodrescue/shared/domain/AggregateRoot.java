package com.foodrescue.shared.domain;

import java.util.List;

/**
 * Marker-Interface für Aggregate Roots.
 *
 * <p>Ein Aggregate Root ist die Wurzel eines Aggregats und dient als einziger Einstiegspunkt für
 * alle Operationen auf dem Aggregat.
 *
 * <p>Verantwortlichkeiten: - Schutz der Aggregate Boundaries - Verwaltung von Domain Events -
 * Durchsetzung von Invarianten
 */
public interface AggregateRoot {

  /**
   * Gibt die eindeutige ID des Aggregats zurück.
   *
   * @return Die Aggregate ID als String
   */
  String getId();

  /**
   * Gibt alle Domain Events zurück, die seit der letzten Speicherung aufgetreten sind.
   *
   * @return Unveränderliche Liste von Domain Events
   */
  List<DomainEvent> getDomainEvents();

  /**
   * Löscht alle Domain Events nach dem Publishing. Diese Methode sollte vom Repository nach dem
   * Speichern aufgerufen werden.
   */
  void clearDomainEvents();
}
