package com.foodrescue.reservierungsmanagement.infrastructure.web.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReserviereAngebotRequestTest {

  @Test
  void gettersSetters_roundTrip() {
    ReserviereAngebotRequest r = new ReserviereAngebotRequest();
    UUID uid = UUID.randomUUID();

    r.setAngebotId("a1");
    r.setAbholerId(uid);

    assertEquals("a1", r.getAngebotId());
    assertEquals(uid, r.getAbholerId());
  }
}
