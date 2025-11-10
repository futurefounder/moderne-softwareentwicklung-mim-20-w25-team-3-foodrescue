package com.foodrescue.domain.events;

import java.time.Instant;

/**
 * Domain Event: Wird ausgelöst, wenn eine Abholung erfolgreich abgeschlossen wurde.
 *
 * <p>Dieses Event signalisiert, dass die Lebensmittel erfolgreich vom Retter abgeholt wurden und
 * der Rettungsvorgang abgeschlossen ist.
 */
public final class AbholungAbgeschlossen implements DomainEvent {
    private final String reservierungsId;
    private final Instant when = Instant.now();
    public AbholungAbgeschlossen(String reservierungsId){
        this.reservierungsId = reservierungsId;
    }
    public String getReservierungsId(){
        return reservierungsId;
    }
    @Override public Instant occurredOn(){
        return when;
    }
}
