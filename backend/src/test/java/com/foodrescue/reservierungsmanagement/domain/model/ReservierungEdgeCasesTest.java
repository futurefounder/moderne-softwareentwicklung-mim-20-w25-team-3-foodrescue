package com.foodrescue.reservierungsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

public class ReservierungEdgeCasesTest {
  @Test
  void stornieren_nichtMehrMoeglich_nachAbholung() {
    var r = Reservierung.erstelle("r1", "a1", "u1", Abholcode.of("AB12"));
    r.bestaetigeAbholung(Abholcode.of("AB12"));
    assertThrows(DomainException.class, r::stornieren);
  }
}
