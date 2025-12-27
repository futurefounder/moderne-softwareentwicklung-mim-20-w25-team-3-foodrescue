package com.foodrescue.reservierungsmanagement.domain.events;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReservierungErstelltTest {

  @Test
  void occurredOn_isNotNull_andIdIsExposed() {
    ReservierungErstellt e = new ReservierungErstellt("r1");
    assertEquals("r1", e.getReservierungsId());
    assertNotNull(e.occurredOn());
  }
}
