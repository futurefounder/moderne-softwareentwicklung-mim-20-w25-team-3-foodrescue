package com.foodrescue.domain.model.ids;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class ReservierungsIdTest {

  @Test
  void createsValidId() {
    ReservierungsId id = ReservierungsId.of("res-99");

    assertEquals("res-99", id.value());
    assertEquals("res-99", id.toString());
  }

  @Test
  void equalIdsAreEqualAndHaveSameHashCode() {
    ReservierungsId id1 = ReservierungsId.of("res-99");
    ReservierungsId id2 = ReservierungsId.of("res-99");
    ReservierungsId idOther = ReservierungsId.of("res-100");

    assertEquals(id1, id2);
    assertEquals(id1.hashCode(), id2.hashCode());
    assertNotEquals(id1, idOther);
    assertNotEquals(null, id1);
    assertNotEquals("x", id1);
  }

  @Test
  void nullValueIsRejected() {
    assertThrows(DomainException.class, () -> ReservierungsId.of(null));
  }

  @Test
  void blankValueIsRejected() {
    assertThrows(DomainException.class, () -> ReservierungsId.of("   "));
  }
}
