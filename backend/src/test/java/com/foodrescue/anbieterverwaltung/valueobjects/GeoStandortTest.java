package com.foodrescue.anbieterverwaltung.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GeoStandortTest {

  @Test
  void erstelltGueltigenGeoStandort() {
    GeoStandort standort = new GeoStandort(50.0, 8.0);

    assertEquals(50.0, standort.getBreitengrad());
    assertEquals(8.0, standort.getLaengengrad());
    assertTrue(standort.toString().contains("50.0"));
  }

  @Test
  void wirftExceptionBeiUngueltigemBreitengrad() {
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(100.0, 8.0));
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(-100.0, 8.0));
  }

  @Test
  void wirftExceptionBeiUngueltigemLaengengrad() {
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(50.0, 200.0));
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(50.0, -200.0));
  }

  @Test
  void equalsUndHashCode() {
    GeoStandort a = new GeoStandort(50.0, 8.0);
    GeoStandort b = new GeoStandort(50.0, 8.0);
    GeoStandort c = new GeoStandort(51.0, 8.0);

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
  }
}
