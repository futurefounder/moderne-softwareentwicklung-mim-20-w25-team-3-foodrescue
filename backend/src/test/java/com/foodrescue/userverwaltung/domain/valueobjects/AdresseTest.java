package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdresseTest {

  @Test
  void ctor_trimsAllFields() {
    Adresse a = new Adresse("  Musterstr. 1 ", " 12345 ", "  Berlin ", " DE ");
    assertEquals("Musterstr. 1", a.getStrasse());
    assertEquals("12345", a.getPlz());
    assertEquals("Berlin", a.getOrt());
    assertEquals("DE", a.getLand());
  }

  @Test
  void ctor_rejectsNullField() {
    NullPointerException ex =
        assertThrows(NullPointerException.class, () -> new Adresse(null, "1", "2", "3"));
    assertTrue(ex.getMessage().contains("Strasse darf nicht null"));
  }

  @Test
  void ctor_rejectsBlankField() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> new Adresse(" ", "123", "Ort", "DE"));
    assertTrue(ex.getMessage().contains("Strasse darf nicht leer"));
  }

  @Test
  void equalsAndHashCode_considerAllFields() {
    Adresse a1 = new Adresse("A", "1", "O", "DE");
    Adresse a2 = new Adresse("A", "1", "O", "DE");
    Adresse a3 = new Adresse("B", "1", "O", "DE");
    assertEquals(a1, a2);
    assertEquals(a1.hashCode(), a2.hashCode());
    assertNotEquals(a1, a3);
  }

  @Test
  void toString_containsAllParts() {
    Adresse a = new Adresse("Ahornstr. 11", "12345", "Berlin", "DE");
    assertEquals("Ahornstr. 11, 12345 Berlin, DE", a.toString());
  }
}
