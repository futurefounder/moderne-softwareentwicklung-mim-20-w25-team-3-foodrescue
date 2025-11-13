package com.foodrescue.domain.model;

import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReservierungEdgeCasesTest {
    @Test
    void stornieren_nichtMehrMoeglich_nachAbholung() {
        var r = Reservierung.erstelle("r1","a1","u1", Abholcode.of("AB12"));
        r.bestaetigeAbholung(Abholcode.of("AB12"));
        assertThrows(DomainException.class, r::stornieren);
    }
}
