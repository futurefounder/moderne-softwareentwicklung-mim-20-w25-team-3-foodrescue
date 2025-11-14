package com.foodrescue.anbieterverwaltung.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class AnbieterProfilIdTest {

  @Test
  void erstelltAnbieterProfilIdMitUuid() {
    UUID uuid = UUID.randomUUID();
    AnbieterProfilId id = new AnbieterProfilId(uuid);

    assertEquals(uuid, id.getValue());
    assertEquals(uuid.toString(), id.toString());
  }

  @Test
  void neuErzeugtNeueId() {
    AnbieterProfilId id1 = AnbieterProfilId.neu();
    AnbieterProfilId id2 = AnbieterProfilId.neu();

    assertNotNull(id1.getValue());
    assertNotNull(id2.getValue());
    assertNotEquals(id1, id2);
  }

  @Test
  void wirftExceptionBeiNull() {
    assertThrows(NullPointerException.class, () -> new AnbieterProfilId(null));
  }

  @Test
  void equalsUndHashCode() {
    UUID uuid = UUID.randomUUID();
    AnbieterProfilId a = new AnbieterProfilId(uuid);
    AnbieterProfilId b = new AnbieterProfilId(uuid);
    AnbieterProfilId c = new AnbieterProfilId(UUID.randomUUID());

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
  }
}
