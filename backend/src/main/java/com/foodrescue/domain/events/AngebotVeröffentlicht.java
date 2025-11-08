package com.foodrescue.domain.events;

import java.time.LocalDateTime;

/**
 * Domain Event: Wird ausgelöst, wenn ein neues Lebensmittelangebot veröffentlicht wurde.
 *
 * <p>Dieses Event signalisiert, dass ein Anbieter ein neues Angebot zur Rettung von Lebensmitteln
 * erstellt und veröffentlicht hat.
 */
public record AngebotVeröffentlicht(Long angebotId, LocalDateTime occurredOn)
    implements DomainEvent {}
