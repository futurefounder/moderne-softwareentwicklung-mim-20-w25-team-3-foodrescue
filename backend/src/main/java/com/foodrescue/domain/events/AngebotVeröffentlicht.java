package com.foodrescue.domain.events;

/**
 * Domain Event: Wird ausgelöst, wenn ein neues Lebensmittelangebot veröffentlicht wurde.
 *
 * <p>Dieses Event signalisiert, dass ein Anbieter ein neues Angebot zur Rettung von Lebensmitteln
 * erstellt und veröffentlicht hat.
 */
import java.time.Instant;

public final class AngebotVeröffentlicht implements DomainEvent {
    private final String angebotsId;
    private final Instant when = Instant.now();

    public AngebotVeröffentlicht(String angebotsId) {
        this.angebotsId = angebotsId;
    }
    public String getAngebotsId(){
        return angebotsId;
    }
    @Override public Instant occurredOn(){
        return when;
    }
}
