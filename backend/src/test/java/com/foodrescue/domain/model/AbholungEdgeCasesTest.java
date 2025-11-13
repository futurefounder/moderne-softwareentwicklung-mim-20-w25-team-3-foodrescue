package com.foodrescue.domain.model;

import com.foodrescue.domain.events.AbholungAbgeschlossen;
import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AbholungEdgeCasesTest {

    @Test
    void constructorRejectsNullArguments() {
        Abholcode code = Abholcode.of("AB12CD");

        assertThrows(DomainException.class, () -> new Abholung(null, "r1", code));
        assertThrows(DomainException.class, () -> new Abholung("h1", null, code));
        assertThrows(DomainException.class, () -> new Abholung("h1", "r1", null));
    }

    @Test
    void bestaetigenFailsAfterAlreadyCompleted() {
        Abholcode code = Abholcode.of("AB12CD");
        Abholung a = new Abholung("h1", "r1", code);

        a.bestaetigen(code); // erster Erfolg

        assertThrows(DomainException.class, () -> a.bestaetigen(code));
        assertEquals(Abholung.Status.ABGESCHLOSSEN, a.getStatus());
    }

    @Test
    void bestaetigenFailsAfterWrongCodeAttempt() {
        Abholcode correct = Abholcode.of("AB12CD");
        Abholcode wrong = Abholcode.of("ZZ99YY");

        Abholung a = new Abholung("h1", "r1", correct);

        assertThrows(DomainException.class, () -> a.bestaetigen(wrong));
        assertEquals(Abholung.Status.FEHLGESCHLAGEN, a.getStatus());

        assertThrows(DomainException.class, () -> a.bestaetigen(correct));
    }

    @Test
    void completedAbholungSetsTimestampWithinExpectedBounds() {
        Abholcode code = Abholcode.of("AB12CD");
        Abholung a = new Abholung("h1", "r1", code);

        Instant before = Instant.now();
        List<?> events = a.bestaetigen(code);
        Instant after = Instant.now();

        assertNotNull(a.getAbgeschlossenAm());
        assertFalse(a.getAbgeschlossenAm().isBefore(before));
        assertFalse(a.getAbgeschlossenAm().isAfter(after));

        assertEquals(1, events.size());
        assertInstanceOf(AbholungAbgeschlossen.class, events.get(0));
    }
}

