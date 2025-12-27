package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NameTest {

  @Test
  void ctor_trims() {
    Name n = new Name("  Max Mustermann  ");
    assertEquals("Max Mustermann", n.getValue());
  }

  @Test
  void ctor_rejectsNull() {
    assertThrows(NullPointerException.class, () -> new Name(null));
  }

  @Test
  void ctor_rejectsBlank() {
    assertThrows(IllegalArgumentException.class, () -> new Name("   "));
  }
}
