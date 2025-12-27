package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GeschaeftsnameTest {

  @Test
  void ctor_trims() {
    Geschaeftsname g = new Geschaeftsname("  Bäckerei Müller  ");
    assertEquals("Bäckerei Müller", g.getValue());
  }

  @Test
  void ctor_rejectsNull() {
    assertThrows(NullPointerException.class, () -> new Geschaeftsname(null));
  }

  @Test
  void ctor_rejectsBlank() {
    assertThrows(IllegalArgumentException.class, () -> new Geschaeftsname("   "));
  }
}
