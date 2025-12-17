package com.foodrescue.reservierungsmanagement.domain.events;

import com.foodrescue.shared.domain.DomainEvent;
import java.time.Instant;

public class ReservierungStorniertEvent implements DomainEvent {
  private final String reservierungsId;
  private final Instant when = Instant.now();

  public ReservierungStorniertEvent(String reservierungsId) {
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
