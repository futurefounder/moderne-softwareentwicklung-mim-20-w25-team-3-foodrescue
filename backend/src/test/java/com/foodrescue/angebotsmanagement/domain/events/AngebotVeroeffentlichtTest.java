package com.foodrescue.angebotsmanagement.domain.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

public class AngebotVeroeffentlichtTest {

  @Test
  void speichertAngebotsIdUndZeitpunkt() {
    AngebotErstelltEvent event = new AngebotErstelltEvent("a1");

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
