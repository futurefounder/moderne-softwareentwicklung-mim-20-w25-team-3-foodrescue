package com.foodrescue.domain.model;

import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AbholZeitfensterEdgeCasesTest {

    @Test
    void rejectsNullStartOrEnd() {
        LocalDateTime now = LocalDateTime.now();

        assertThrows(DomainException.class, () -> AbholZeitfenster.of(null, now.plusHours(1)));
        assertThrows(DomainException.class, () -> AbholZeitfenster.of(now, null));
    }

    @Test
    void rejectsEndBeforeOrEqualStart() {
        LocalDateTime t = LocalDateTime.now();

        assertThrows(DomainException.class, () -> AbholZeitfenster.of(t, t));
        assertThrows(DomainException.class, () -> AbholZeitfenster.of(t.plusHours(1), t));
    }

    @Test
    void istNochAktuellReturnsCorrectValuesAroundBoundary() {
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusSeconds(1);

        AbholZeitfenster z = AbholZeitfenster.of(start, end);

        assertTrue(z.istNochAktuell(LocalDateTime.now()));
        assertFalse(z.istNochAktuell(end.plusSeconds(1)));
    }

    @Test
    void equalsAndHashcodeConsiderBothBounds() {
        LocalDateTime s = LocalDateTime.now();
        LocalDateTime e = s.plusHours(1);

        AbholZeitfenster z1 = AbholZeitfenster.of(s, e);
        AbholZeitfenster z2 = AbholZeitfenster.of(s, e);
        AbholZeitfenster z3 = AbholZeitfenster.of(s.plusMinutes(1), e);

        assertEquals(z1, z2);
        assertEquals(z1.hashCode(), z2.hashCode());
        assertNotEquals(z1, z3);
    }
}

