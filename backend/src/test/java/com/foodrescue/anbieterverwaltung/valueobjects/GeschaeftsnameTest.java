package com.foodrescue.anbieterverwaltung.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GeschaeftsnameTest {

  @Test
  void erstelltGueltigenGeschaeftsname() {
    Geschaeftsname name = new Geschaeftsname("  Baeckerei  ");
    assertEquals("Baeckerei", name.getValue());
    assertEquals("Baeckerei", name.toString());
  }

  @Test
  void wirftExceptionBeiNull() {
    assertThrows(NullPointerException.class, () -> new Geschaeftsname(null));
  }

  @Test
  void wirftExceptionBeiLeer() {
    assertThrows(IllegalArgumentException.class, () -> new Geschaeftsname("   "));
  }

  @Test
  void equalsUndHashCode() {
    Geschaeftsname a = new Geschaeftsname("Baeckerei");
    Geschaeftsname b = new Geschaeftsname("Baeckerei");
    Geschaeftsname c = new Geschaeftsname("Supermarkt");

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
  }
}
