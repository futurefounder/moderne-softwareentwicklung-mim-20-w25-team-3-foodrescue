package com.foodrescue.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class ReservierungErstelltTest {

    @Test
    void speichertReservierungsIdUndZeitpunkt() {
        ReservierungErstellt event = new ReservierungErstellt("r1");

        // nutzt getReservierungsId()
        assertEquals("r1", event.getReservierungsId());

        // nutzt occurredOn()
        Instant first = event.occurredOn();
        Instant second = event.occurredOn();

        assertNotNull(first);
        assertEquals(first, second);
        assertFalse(first.isAfter(Instant.now().plusSeconds(1)));
    }
}
