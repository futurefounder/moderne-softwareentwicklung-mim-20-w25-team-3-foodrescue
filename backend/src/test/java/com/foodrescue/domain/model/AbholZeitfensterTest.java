package com.foodrescue.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.exceptions.DomainException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class AbholZeitfensterTest {

  @Test
  void validZeitfensterIsCreated() {
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = start.plusHours(2);

    AbholZeitfenster z = AbholZeitfenster.of(start, end);

    assertEquals(start, z.von());
    assertEquals(end, z.bis());
  }

  @Test
  void nullStartThrows() {
    LocalDateTime end = LocalDateTime.now().plusHours(2);
    assertThrows(DomainException.class, () -> AbholZeitfenster.of(null, end));
  }

  @Test
  void nullEndThrows() {
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    assertThrows(DomainException.class, () -> AbholZeitfenster.of(start, null));
  }

  @Test
  void endNotAfterStartThrows() {
    LocalDateTime t = LocalDateTime.now().plusHours(1);
    assertThrows(DomainException.class, () -> AbholZeitfenster.of(t, t)); // gleich
    assertThrows(DomainException.class, () -> AbholZeitfenster.of(t, t.minusHours(1))); // vorher
  }

  @Test
  void istNochAktuellReturnsTrueWhenJetztIsBeforeBis() {
    LocalDateTime start = LocalDateTime.now().minusHours(1);
    LocalDateTime end = LocalDateTime.now().plusHours(1);

    AbholZeitfenster z = AbholZeitfenster.of(start, end);

    assertTrue(z.istNochAktuell(LocalDateTime.now()));
  }

  @Test
  void istNochAktuellReturnsFalseWhenJetztIsAfterBis() {
    LocalDateTime start = LocalDateTime.now().minusHours(3);
    LocalDateTime end = LocalDateTime.now().minusHours(1);

    AbholZeitfenster z = AbholZeitfenster.of(start, end);

    assertFalse(z.istNochAktuell(LocalDateTime.now()));
  }

  @Test
  void equalsAndHashCodeWork() {
    LocalDateTime start = LocalDateTime.now().plusHours(1);
    LocalDateTime end = start.plusHours(1);

    AbholZeitfenster z1 = AbholZeitfenster.of(start, end);
    AbholZeitfenster z2 = AbholZeitfenster.of(start, end);
    AbholZeitfenster z3 = AbholZeitfenster.of(start.plusMinutes(1), end.plusMinutes(1));

    assertEquals(z1, z2);
    assertEquals(z1.hashCode(), z2.hashCode());
    assertNotEquals(z1, z3);
    assertNotEquals(z1, null);
    assertNotEquals(z1, "other");
  }
}
