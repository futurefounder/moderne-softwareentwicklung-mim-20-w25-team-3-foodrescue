package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import org.junit.jupiter.api.Test;

public class EmailAdresseTest {

  @Test
  void erstelltGueltigeEmail() {
    EmailAdresse email = new EmailAdresse(" test@example.com ");
    assertEquals("test@example.com", email.getValue());
    assertEquals("example.com", email.getDomain());
    assertEquals("test@example.com", email.toString());
  }

  @Test
  void wirftExceptionBeiNull() {
    assertThrows(NullPointerException.class, () -> new EmailAdresse(null));
  }

  @Test
  void wirftExceptionBeiLeer() {
    assertThrows(IllegalArgumentException.class, () -> new EmailAdresse("   "));
  }

  @Test
  void wirftExceptionBeiUngueltigemFormat() {
    assertThrows(IllegalArgumentException.class, () -> new EmailAdresse("keine-email"));
  }

  @Test
  void equalsIstCaseInsensitive() {
    EmailAdresse a = new EmailAdresse("TEST@example.com");
    EmailAdresse b = new EmailAdresse("test@example.com");

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }
}
