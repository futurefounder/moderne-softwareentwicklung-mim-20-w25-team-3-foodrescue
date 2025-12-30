package com.foodrescue.angebotsmanagement.domain.events;

import com.foodrescue.shared.domain.DomainEvent;
import java.time.Instant;
import java.util.Objects;

/**
 * Domain Event: Ein Angebot wurde reserviert.
 *
 * <p>Dieses Event wird ausgelöst, wenn ein Angebot erfolgreich von einem Abholer reserviert wurde.
 * Das Event kapselt alle notwendigen Informationen, damit andere Bounded Contexts (z.B.
 * Reservierungsmanagement) darauf reagieren können.
 */
public class AngebotReserviertEvent implements DomainEvent {

  private final String angebotId;
  private final String abholerId;
  private final String abholcode;
  private final Instant occurredOn;

  public AngebotReserviertEvent(String angebotId, String abholerId, String abholcode) {
    this.angebotId = Objects.requireNonNull(angebotId, "AngebotId darf nicht null sein");
    this.abholerId = Objects.requireNonNull(abholerId, "AbholerId darf nicht null sein");
    this.abholcode = Objects.requireNonNull(abholcode, "Abholcode darf nicht null sein");
    this.occurredOn = Instant.now();
  }

  @Override
  public Instant occurredOn() {
    return occurredOn;
  }

  public String getAngebotId() {
    return angebotId;
  }

  public String getAbholerId() {
    return abholerId;
  }

  public String getAbholcode() {
    return abholcode;
  }

  @Override
  public String toString() {
    return "AngebotReserviertEvent{"
        + "angebotId='"
        + angebotId
        + '\''
        + ", abholerId='"
        + abholerId
        + '\''
        + ", occurredOn="
        + occurredOn
        + '}';
  }
}
