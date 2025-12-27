package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserIdTest {

  @Test
  void neu_createsDifferentIds() {
    UserId a = UserId.neu();
    UserId b = UserId.neu();
    assertNotEquals(a, b);
  }

  @Test
  void ctor_rejectsNull() {
    assertThrows(RuntimeException.class, () -> new UserId(null));
  }

  @Test
  void equalsAndHashCode_useUuid() {
    UUID uuid = UUID.randomUUID();
    UserId a = new UserId(uuid);
    UserId b = new UserId(uuid);
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }
}
