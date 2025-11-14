package com.foodrescue.angebotsmanagement.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

class AngebotsIdTest {

  @Test
  void createsValidId() {
    AngebotsId id = AngebotsId.of("ang-1");

    assertEquals("ang-1", id.value());
    assertEquals("ang-1", id.toString());
  }

  @Test
  void equalIdsAreEqualAndHaveSameHashCode() {
    AngebotsId id1 = AngebotsId.of("ang-1");
    AngebotsId id2 = AngebotsId.of("ang-1");
    AngebotsId idOther = AngebotsId.of("ang-2");

    assertEquals(id1, id2);
    assertEquals(id1.hashCode(), id2.hashCode());
    assertNotEquals(id1, idOther);
    assertNotEquals(null, id1);
    assertNotEquals("other", id1);
  }

  @Test
  void nullValueIsRejected() {
    assertThrows(DomainException.class, () -> AngebotsId.of(null));
  }

  @Test
  void blankValueIsRejected() {
    assertThrows(DomainException.class, () -> AngebotsId.of("   "));
  }
}
