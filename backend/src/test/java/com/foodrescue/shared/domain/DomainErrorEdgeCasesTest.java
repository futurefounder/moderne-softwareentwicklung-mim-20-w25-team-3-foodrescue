package com.foodrescue.shared.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DomainErrorEdgeCasesTest {

  @Test
  void eachErrorHasNonBlankDefaultMessage() {
    for (DomainError error : DomainError.values()) {
      assertNotNull(error.defaultMessage(), error.name() + " should have a default message");
      assertFalse(
          error.defaultMessage().isBlank(),
          error.name() + " should not have a blank default message");
    }
  }

  @Test
  void invalidEnumNameThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> DomainError.valueOf("NOT_A_REAL_ERROR"));
  }

  @Test
  void enumHasExpectedNumberOfValues() {
    // Achtung: Dieser Test zeigt an, wenn später neue Fehler hinzugefügt werden,
    // damit man die Testabdeckung ergänzt.
    assertEquals(8, DomainError.values().length);
  }
}
