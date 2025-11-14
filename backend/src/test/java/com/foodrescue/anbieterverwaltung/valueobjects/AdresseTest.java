package com.foodrescue.anbieterverwaltung.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AdresseTest {

  @Test
  void erstelltGueltigeAdresse() {
    Adresse adresse = new Adresse("Strasse 1", "12345", "Stadt", "DE");

    assertEquals("Strasse 1", adresse.getStrasse());
    assertEquals("12345", adresse.getPlz());
    assertEquals("Stadt", adresse.getOrt());
    assertEquals("DE", adresse.getLand());
    assertTrue(adresse.toString().contains("Strasse 1"));
  }

  @Test
  void wirftExceptionBeiNullFeld() {
    assertThrows(NullPointerException.class, () -> new Adresse(null, "12345", "Stadt", "DE"));
    assertThrows(NullPointerException.class, () -> new Adresse("Strasse 1", null, "Stadt", "DE"));
    assertThrows(NullPointerException.class, () -> new Adresse("Strasse 1", "12345", null, "DE"));
    assertThrows(
        NullPointerException.class, () -> new Adresse("Strasse 1", "12345", "Stadt", null));
  }

  @Test
  void wirftExceptionBeiLeerenFeldern() {
    assertThrows(IllegalArgumentException.class, () -> new Adresse("   ", "12345", "Stadt", "DE"));
    assertThrows(
        IllegalArgumentException.class, () -> new Adresse("Strasse 1", "   ", "Stadt", "DE"));
    assertThrows(
        IllegalArgumentException.class, () -> new Adresse("Strasse 1", "12345", "   ", "DE"));
    assertThrows(
        IllegalArgumentException.class, () -> new Adresse("Strasse 1", "12345", "Stadt", "   "));
  }

  @Test
  void equalsUndHashCode() {
    Adresse a = new Adresse("Strasse 1", "12345", "Stadt", "DE");
    Adresse b = new Adresse("Strasse 1", "12345", "Stadt", "DE");
    Adresse c = new Adresse("Strasse 2", "12345", "Stadt", "DE");

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
  }
}
