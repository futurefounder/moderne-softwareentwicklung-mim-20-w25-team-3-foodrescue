package com.foodrescue.domain.model;

import com.foodrescue.domain.events.AbholungAbgeschlossen;
import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbholungTest {

    @Test
    void bestaetigen_mitKorrektCode_setztStatusAbgeschlossen_und_emittiertEvent() {
        var code = Abholcode.of("XZ12");
        var abholung = new Abholung("h1", "r1", code);

        List<?> events = abholung.bestaetigen(Abholcode.of("XZ12"));

        assertEquals(Abholung.Status.ABGESCHLOSSEN, abholung.getStatus());
        assertTrue(events.stream().anyMatch(e -> e instanceof AbholungAbgeschlossen));
        assertNotNull(abholung.getAbgeschlossenAm());
    }

    @Test
    void bestaetigen_mitFalschemCode_setztStatusFehlgeschlagen_und_wirftFehler() {
        var abholung = new Abholung("h1", "r1", Abholcode.of("AB12"));

        var ex = assertThrows(DomainException.class, () ->
                abholung.bestaetigen(Abholcode.of("ZZ99")));

        assertEquals("Falscher Abholcode", ex.getMessage());
        assertEquals(Abholung.Status.FEHLGESCHLAGEN, abholung.getStatus());
    }

    @Test
    void doppelteBestaetigung_wirftFehler() {
        var code = Abholcode.of("AB12");
        var abholung = new Abholung("h1", "r1", code);
        abholung.bestaetigen(code);

        assertThrows(DomainException.class, () -> abholung.bestaetigen(code));
    }
}
