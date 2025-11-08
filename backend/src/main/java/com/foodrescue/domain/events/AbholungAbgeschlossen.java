package com.foodrescue.domain.events;

import java.time.LocalDateTime;

/**
 * Domain Event: Wird ausgelöst, wenn eine Abholung erfolgreich abgeschlossen wurde.
 *
 * <p>Dieses Event signalisiert, dass die Lebensmittel erfolgreich vom Retter abgeholt wurden und
 * der Rettungsvorgang abgeschlossen ist.
 */
public record AbholungAbgeschlossen(Long abholungId, Long reservierungId, LocalDateTime occurredOn)
    implements DomainEvent {}
