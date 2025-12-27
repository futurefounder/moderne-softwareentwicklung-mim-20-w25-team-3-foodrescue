package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class AnbieterProfilIdTest {

  @Test
  void neu_createsDifferentIds() {
    AnbieterProfilId a = AnbieterProfilId.neu();
    AnbieterProfilId b = AnbieterProfilId.neu();
    assertNotEquals(a, b);
  }

  @Test
  void ctor_rejectsNull() {
    assertThrows(NullPointerException.class, () -> new AnbieterProfilId(null));
  }

  @Test
  void equalsAndHashCode_useUuid() {
    UUID uuid = UUID.randomUUID();
    AnbieterProfilId a = new AnbieterProfilId(uuid);
    AnbieterProfilId b = new AnbieterProfilId(uuid);
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }
}
