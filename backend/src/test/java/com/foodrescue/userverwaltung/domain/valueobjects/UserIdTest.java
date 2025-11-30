package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.shared.exception.DomainException;
import java.util.UUID;

import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import org.junit.jupiter.api.Test;

public class UserIdTest {

  @Test
  void erstelltUserIdMitUuid() {
    UUID uuid = UUID.randomUUID();
    UserId id = new UserId(uuid);

    assertEquals(uuid, id.getValue());
    assertEquals(uuid.toString(), id.toString());
  }

  @Test
  void neuErzeugtNeueId() {
    UserId id1 = UserId.neu();
    UserId id2 = UserId.neu();

    assertNotNull(id1.getValue());
    assertNotNull(id2.getValue());
    assertNotEquals(id1, id2);
  }

  @Test
  void wirftExceptionBeiNull() {
    assertThrows(DomainException.class, () -> new UserId(null));
  }

  @Test
  void equalsUndHashCode() {
    UUID uuid = UUID.randomUUID();
    UserId a = new UserId(uuid);
    UserId b = new UserId(uuid);
    UserId c = new UserId(UUID.randomUUID());

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
  }
}
