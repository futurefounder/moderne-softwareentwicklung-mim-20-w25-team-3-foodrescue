package com.foodrescue.domain.events;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class AngebotVeroeffentlichtTest {

    @Test
    void speichertAngebotsIdUndZeitpunkt() {
        AngebotVeroeffentlicht event = new AngebotVeroeffentlicht("a1");

        // nutzt getAngebotsId()
        assertEquals("a1", event.getAngebotsId());

        // nutzt occurredOn()
        Instant first = event.occurredOn();
        Instant second = event.occurredOn();

        assertNotNull(first);
        assertEquals(first, second);
        assertFalse(first.isAfter(Instant.now().plusSeconds(1)));
    }
}

