package com.foodrescue.domain.model;

import com.foodrescue.domain.events.AbholungAbgeschlossen;
import com.foodrescue.domain.events.ReservierungErstellt;
import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservierungTest {

    @Test
    void erstellen_emittiertEvent() {
        var r = Reservierung.erstelle("r1", "a1", "user1", Abholcode.of("AB12"));

        assertEquals(Reservierung.Status.AKTIV, r.getStatus());
        assertTrue(r.getDomainEvents().stream().anyMatch(e -> e instanceof ReservierungErstellt));
    }

    @Test
    void bestaetigeAbholung_mitKorrektCode_setztStatusAbgeholt_und_emittiertEvent() {
        var code = Abholcode.of("AB12");
        var r = Reservierung.erstelle("r1", "a1", "user1", code);

        var events = r.bestaetigeAbholung(code);

        assertEquals(Reservierung.Status.ABGEHOLT, r.getStatus());
        assertTrue(events.stream().anyMatch(e -> e instanceof AbholungAbgeschlossen));
    }

    @Test
    void bestaetigeAbholung_mitFalschemCode_wirftFehler() {
        var r = Reservierung.erstelle("r1", "a1", "user1", Abholcode.of("AB12"));
        assertThrows(DomainException.class, () -> r.bestaetigeAbholung(Abholcode.of("ZZ99")));
    }

    @Test
    void stornieren_setztStatusStorniert() {
        var r = Reservierung.erstelle("r1", "a1", "user1", Abholcode.of("AB12"));
        r.stornieren();
        assertEquals(Reservierung.Status.STORNIERT, r.getStatus());
    }
}
