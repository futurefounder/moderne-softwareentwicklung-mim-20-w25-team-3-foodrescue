package com.foodrescue.abholungsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

class AbholcodeTest {

  @Test
  void of_acceptsValidUppercaseAlnum_4to8() {
    assertEquals("AB12", Abholcode.of("AB12").value());
    assertEquals("A1B2C3", Abholcode.of("A1B2C3").toString());
    assertEquals("ABCDEFG8", Abholcode.of("ABCDEFG8").value());
  }

  @Test
  void of_rejectsNull() {
    assertThrows(DomainException.class, () -> Abholcode.of(null));
  }

  @Test
  void of_rejectsLowercaseOrSpecialCharsOrWrongLength() {
    assertAll(
        () -> assertThrows(DomainException.class, () -> Abholcode.of("ab12")),
        () -> assertThrows(DomainException.class, () -> Abholcode.of("AB-1")),
        () -> assertThrows(DomainException.class, () -> Abholcode.of("ABC")),
        () -> assertThrows(DomainException.class, () -> Abholcode.of("ABCDEFGHI")));
  }

  @Test
  void random_generates6CharsFromAllowedAlphabet_andIsValid() {
    Abholcode c = Abholcode.random();
    assertNotNull(c);
    assertNotNull(c.value());
    assertEquals(6, c.value().length());

    String allowed = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    for (char ch : c.value().toCharArray()) {
      assertTrue(allowed.indexOf(ch) >= 0, "Unexpected char: " + ch);
    }

    assertDoesNotThrow(() -> Abholcode.of(c.value()));
  }

  @Test
  void equalsAndHashCode_basedOnValue() {
    Abholcode a = Abholcode.of("AB12");
    Abholcode b = Abholcode.of("AB12");
    Abholcode c = Abholcode.of("CD34");

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
    assertNotEquals(a, null);
    assertNotEquals(a, "AB12");
  }
}
