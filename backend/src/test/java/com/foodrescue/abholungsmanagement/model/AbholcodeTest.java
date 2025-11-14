package com.foodrescue.abholungsmanagement.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

public class AbholcodeTest {

  @Test
  void validCodeIsCreated() {
    Abholcode code = Abholcode.of("AB12");
    assertEquals("AB12", code.value());
    assertEquals("AB12", code.toString());
  }

  @Test
  void nullValueThrowsException() {
    assertThrows(DomainException.class, () -> Abholcode.of(null));
  }

  @Test
  void tooShortCodeThrows() {
    assertThrows(DomainException.class, () -> Abholcode.of("A1"));
  }

  @Test
  void tooLongCodeThrows() {
    assertThrows(DomainException.class, () -> Abholcode.of("ABCDEFGH9")); // 9 chars
  }

  @Test
  void lowercaseLettersAreRejected() {
    assertThrows(DomainException.class, () -> Abholcode.of("ab12CD"));
  }

  @Test
  void invalidCharactersAreRejected() {
    assertThrows(DomainException.class, () -> Abholcode.of("A$12"));
    assertThrows(DomainException.class, () -> Abholcode.of("A_12"));
    assertThrows(DomainException.class, () -> Abholcode.of("Aä12"));
  }

  @Test
  void equalsAndHashCode() {
    Abholcode c1 = Abholcode.of("AB12");
    Abholcode c2 = Abholcode.of("AB12");
    Abholcode c3 = Abholcode.of("CD34");

    assertEquals(c1, c2);
    assertEquals(c1.hashCode(), c2.hashCode());
    assertNotEquals(c1, c3);
    assertNotEquals(c1, null);
    assertNotEquals(c1, "AB12");
  }

  @Test
  void randomGeneratesValidCode() {
    Abholcode r = Abholcode.random();

    assertNotNull(r.value());
    assertTrue(
        r.value().matches("[A-Z0-9]{4,8}"),
        "Random code must match regex [A-Z0-9]{4,8} but was: " + r.value());
  }

  @Test
  void randomGeneratesDifferentCodesMostOfTheTime() {
    Abholcode r1 = Abholcode.random();
    Abholcode r2 = Abholcode.random();

    // Nicht garantiert, aber extrem unwahrscheinlich: zwei gleiche Codes
    assertNotEquals(r1, r2);
  }
}
