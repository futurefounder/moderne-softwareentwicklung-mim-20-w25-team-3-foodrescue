package com.foodrescue.domain.events;

import java.time.LocalDateTime;

/**
 * Domain Event: Wird ausgelöst, wenn eine Reservierung für ein Angebot erstellt wurde.
 *
 * <p>Dieses Event signalisiert, dass ein Retter ein verfügbares Angebot reserviert hat und es zur
 * Abholung vorgemerkt wurde.
 */
public record ReservierungErstellt(
    Long reservierungId, Long angebotId, LocalDateTime occurredOn) implements DomainEvent {}

