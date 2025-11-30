package com.foodrescue.abholungsmanagement.domain.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import com.foodrescue.abholungsmanagement.domain.events.AbholungAbgeschlossen;
import org.junit.jupiter.api.Test;

public class AbholungAbgeschlossenTest {

  @Test
  void speichertReservierungsIdUndZeitpunkt() {
    AbholungAbgeschlossen event = new AbholungAbgeschlossen("r1");

    // nutzt getReservierungsId()
    assertEquals("r1", event.getReservierungsId());

    // nutzt occurredOn()
    Instant first = event.occurredOn();
    Instant second = event.occurredOn();

    assertNotNull(first);
    // Zeitstempel ist im Objekt fix gespeichert
    assertEquals(first, second);
    // grobe Plausibilitätsprüfung: liegt nicht in der Zukunft
    assertFalse(first.isAfter(Instant.now().plusSeconds(1)));
  }
}
