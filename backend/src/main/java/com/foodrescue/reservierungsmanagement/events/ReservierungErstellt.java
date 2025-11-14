package com.foodrescue.reservierungsmanagement.events;

import com.foodrescue.shared.domain.DomainEvent;
import java.time.Instant;

/**
 * Domain Event: Wird ausgelöst, wenn eine Reservierung für ein Angebot erstellt wurde.
 *
 * <p>Dieses Event signalisiert, dass ein Retter ein verfügbares Angebot reserviert hat und es zur
 * Abholung vorgemerkt wurde.
 */
public final class ReservierungErstellt implements DomainEvent {
  private final String reservierungsId;
  private final Instant when = Instant.now();

  public ReservierungErstellt(String reservierungsId) {
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
