package com.foodrescue.angebotsmanagement.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

class AngebotsIdTest {

  @Test
  void constructor_acceptsNonBlankValue() {
    AngebotsId id = new AngebotsId("ang-1");
    assertEquals("ang-1", id.value());
    assertEquals("ang-1", id.toString());
  }

  @Test
  void constructor_rejectsNullOrBlank() {
    assertAll(
        () -> assertThrows(DomainException.class, () -> new AngebotsId(null)),
        () -> assertThrows(DomainException.class, () -> new AngebotsId("")),
        () -> assertThrows(DomainException.class, () -> new AngebotsId("   ")));
  }

  @Test
  void of_delegatesToConstructor() {
    AngebotsId id = AngebotsId.of("ang-2");
    assertEquals("ang-2", id.value());
  }

  @Test
  void equalsAndHashCode_areBasedOnValue() {
    AngebotsId a = AngebotsId.of("same");
    AngebotsId b = AngebotsId.of("same");
    AngebotsId c = AngebotsId.of("other");

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
    assertNotEquals(a, null);
    assertNotEquals(a, "same");
  }
}
