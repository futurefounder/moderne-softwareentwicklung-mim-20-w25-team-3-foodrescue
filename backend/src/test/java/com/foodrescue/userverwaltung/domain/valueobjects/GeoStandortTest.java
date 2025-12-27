package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GeoStandortTest {

  @Test
  void ctor_acceptsBoundaryValues() {
    assertDoesNotThrow(() -> new GeoStandort(-90.0, -180.0));
    assertDoesNotThrow(() -> new GeoStandort(90.0, 180.0));
  }

  @Test
  void ctor_rejectsLatitudeOutOfRange() {
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(-90.0001, 0));
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(90.0001, 0));
  }

  @Test
  void ctor_rejectsLongitudeOutOfRange() {
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(0, -180.0001));
    assertThrows(IllegalArgumentException.class, () -> new GeoStandort(0, 180.0001));
  }

  @Test
  void equalsAndHashCode_useCoordinates() {
    GeoStandort g1 = new GeoStandort(52.5, 13.4);
    GeoStandort g2 = new GeoStandort(52.5, 13.4);
    GeoStandort g3 = new GeoStandort(52.5, 13.4001);
    assertEquals(g1, g2);
    assertEquals(g1.hashCode(), g2.hashCode());
    assertNotEquals(g1, g3);
  }
}
