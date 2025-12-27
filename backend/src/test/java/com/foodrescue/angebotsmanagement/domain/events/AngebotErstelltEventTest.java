package com.foodrescue.angebotsmanagement.domain.events;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class AngebotErstelltEventTest {

  @Test
  void occurredOn_isSetAndStable_andIdIsReturned() throws Exception {
    Instant before = Instant.now();
    AngebotErstelltEvent e = new AngebotErstelltEvent("a1");
    Instant after = Instant.now();

    assertEquals("a1", e.getAngebotsId());
    assertNotNull(e.occurredOn());

    // occurredOn should be within [before, after]
    assertFalse(e.occurredOn().isBefore(before));
    assertFalse(e.occurredOn().isAfter(after));

    // stable timestamp (same instance)
    assertSame(e.occurredOn(), e.occurredOn());
  }
}
