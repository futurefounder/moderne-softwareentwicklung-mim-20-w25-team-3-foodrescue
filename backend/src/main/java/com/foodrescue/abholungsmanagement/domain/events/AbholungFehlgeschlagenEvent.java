package com.foodrescue.abholungsmanagement.domain.events;

import com.foodrescue.shared.domain.DomainEvent;
import java.time.Instant;

public class AbholungFehlgeschlagenEvent implements DomainEvent {
  private final String reservierungsId;
  private final Instant when = Instant.now();

  public AbholungFehlgeschlagenEvent(String reservierungsId) {
    this.reservierungsId = reservierungsId;
  }

  public String getReservierungsId() {
    return reservierungsId;
  }

  @Override
  public Instant occurredOn() {
    return when;
  }
}
