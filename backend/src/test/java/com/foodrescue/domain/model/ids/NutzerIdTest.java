package com.foodrescue.domain.model.ids;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class NutzerIdTest {

  @Test
  void createsValidId() {
    NutzerId id = NutzerId.of("user-42");

    assertEquals("user-42", id.value());
    assertEquals("user-42", id.toString());
  }

  @Test
  void equalIdsAreEqualAndHaveSameHashCode() {
    NutzerId id1 = NutzerId.of("user-42");
    NutzerId id2 = NutzerId.of("user-42");
    NutzerId idOther = NutzerId.of("user-43");

    assertEquals(id1, id2);
    assertEquals(id1.hashCode(), id2.hashCode());
    assertNotEquals(id1, idOther);
    assertNotEquals(null, id1);
    assertNotEquals("x", id1);
  }

  @Test
  void nullValueIsRejected() {
    assertThrows(DomainException.class, () -> NutzerId.of(null));
  }

  @Test
  void blankValueIsRejected() {
    assertThrows(DomainException.class, () -> NutzerId.of("   "));
  }
}
