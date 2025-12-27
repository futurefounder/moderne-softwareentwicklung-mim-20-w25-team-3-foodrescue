package com.foodrescue.userverwaltung.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EmailAdresseTest {

  @Test
  void ctor_rejectsNull() {
    NullPointerException ex =
        assertThrows(NullPointerException.class, () -> new EmailAdresse(null));
    assertTrue(ex.getMessage().contains("Email darf nicht null"));
  }

  @Test
  void ctor_rejectsBlank() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> new EmailAdresse("   "));
    assertTrue(ex.getMessage().contains("Email darf nicht leer"));
  }

  @Test
  void ctor_rejectsInvalidFormat() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> new EmailAdresse("invalid"));
    assertTrue(ex.getMessage().contains("ungültiges Format"));
  }

  @Test
  void ctor_trimsAndStoresValue() {
    EmailAdresse e = new EmailAdresse("  Test@Example.com  ");
    assertEquals("Test@Example.com", e.getValue());
    assertEquals("Example.com", e.getDomain());
  }

  @Test
  void equals_isCaseInsensitive() {
    EmailAdresse a = new EmailAdresse("User@Example.com");
    EmailAdresse b = new EmailAdresse("user@example.com");
    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }
}
