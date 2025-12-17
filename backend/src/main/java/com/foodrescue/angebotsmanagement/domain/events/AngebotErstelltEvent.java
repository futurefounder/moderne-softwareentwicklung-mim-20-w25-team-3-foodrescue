package com.foodrescue.angebotsmanagement.domain.events;

import com.foodrescue.shared.domain.DomainEvent;
import java.time.Instant;

/**
 * Domain Event: Wird ausgelöst, wenn ein neues Lebensmittelangebot veröffentlicht wurde.
 *
 * <p>Dieses Event signalisiert, dass ein Anbieter ein neues Angebot zur Rettung von Lebensmitteln
 * erstellt und veröffentlicht hat.
 */
public class AngebotErstelltEvent implements DomainEvent {
  private final String angebotsId;
  private final Instant when = Instant.now();

  public AngebotErstelltEvent(String angebotsId) {
    this.angebotsId = angebotsId;
  }

  public String getAngebotsId() {
    return angebotsId;
  }

  @Override
  public Instant occurredOn() {
    return when;
  }
}
