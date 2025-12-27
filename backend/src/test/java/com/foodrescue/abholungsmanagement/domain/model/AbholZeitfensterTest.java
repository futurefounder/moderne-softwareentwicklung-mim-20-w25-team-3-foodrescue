package com.foodrescue.abholungsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.shared.exception.DomainException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class AbholZeitfensterTest {

  @Test
  void constructor_rejectsNullsOrInvalidOrder() {
    LocalDateTime t0 = LocalDateTime.of(2025, 1, 1, 10, 0);
    LocalDateTime t1 = LocalDateTime.of(2025, 1, 1, 11, 0);

    assertAll(
        () -> assertThrows(DomainException.class, () -> new AbholZeitfenster(null, t1)),
        () -> assertThrows(DomainException.class, () -> new AbholZeitfenster(t0, null)),
        () -> assertThrows(DomainException.class, () -> new AbholZeitfenster(t0, t0)),
        () -> assertThrows(DomainException.class, () -> new AbholZeitfenster(t1, t0)));
  }

  @Test
  void istNochAktuell_trueBeforeBis_falseAtOrAfterBis() {
    LocalDateTime von = LocalDateTime.of(2025, 1, 1, 10, 0);
    LocalDateTime bis = LocalDateTime.of(2025, 1, 1, 11, 0);
    AbholZeitfenster z = AbholZeitfenster.of(von, bis);

    assertTrue(z.istNochAktuell(LocalDateTime.of(2025, 1, 1, 10, 59, 59)));
    assertFalse(z.istNochAktuell(bis));
    assertFalse(z.istNochAktuell(bis.plusSeconds(1)));
  }

  @Test
  void equalsAndHashCode_useVonBis() {
    LocalDateTime von = LocalDateTime.of(2025, 1, 1, 10, 0);
    LocalDateTime bis = LocalDateTime.of(2025, 1, 1, 11, 0);

    AbholZeitfenster a = AbholZeitfenster.of(von, bis);
    AbholZeitfenster b = AbholZeitfenster.of(von, bis);
    AbholZeitfenster c = AbholZeitfenster.of(von, bis.plusMinutes(1));

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
  }
}
