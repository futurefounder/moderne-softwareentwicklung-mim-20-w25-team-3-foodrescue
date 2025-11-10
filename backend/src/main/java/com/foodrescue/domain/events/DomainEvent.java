package com.foodrescue.domain.events;

import java.time.Instant;

/**
 * Basis-Interface für alle Domain Events im FoodRescue System.
 *
 * <p>Domain Events repräsentieren bedeutende Ereignisse in der Domäne, die bereits stattgefunden
 * haben.
 */
public interface DomainEvent {

  /**
   * Gibt den Zeitpunkt zurück, zu dem das Event aufgetreten ist.
   *
   * @return Der Zeitstempel des Events
   */
  Instant occurredOn();
}
