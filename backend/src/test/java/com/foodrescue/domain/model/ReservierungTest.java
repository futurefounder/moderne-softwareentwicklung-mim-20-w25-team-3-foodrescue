package com.foodrescue.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.domain.events.AbholungAbgeschlossen;
import com.foodrescue.domain.events.ReservierungErstellt;
import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;

public class ReservierungTest {

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

  @Test
  void constructorRejectsNulls() {
    assertThrows(
        DomainException.class, () -> Reservierung.erstelle(null, "a", "u", Abholcode.of("AB12")));
    assertThrows(
        DomainException.class, () -> Reservierung.erstelle("r", null, "u", Abholcode.of("AB12")));
    assertThrows(
        DomainException.class, () -> Reservierung.erstelle("r", "a", null, Abholcode.of("AB12")));
    assertThrows(DomainException.class, () -> Reservierung.erstelle("r", "a", "u", null));
  }

  @Test
  void bestaetigen_rejectIfNotActive() {
    var code = Abholcode.of("AB12");
    var r = Reservierung.erstelle("r1", "a1", "u1", code);
    r.stornieren(); // status = STORNIERT

    assertThrows(DomainException.class, () -> r.bestaetigeAbholung(code));
  }

  @Test
  void stornieren_setsStatus() {
    var r = Reservierung.erstelle("r1", "a1", "u1", Abholcode.of("AB12"));

    r.stornieren();

    assertEquals(Reservierung.Status.STORNIERT, r.getStatus());
  }

  @Test
  void bestaetigen_createsEvent() {
    var code = Abholcode.of("AB12");
    var r = Reservierung.erstelle("r1", "a1", "u1", code);

    var events = r.bestaetigeAbholung(code);

    assertInstanceOf(AbholungAbgeschlossen.class, events.get(0));
    assertEquals(2, r.getDomainEvents().size()); // ReservierungErstellt + AbholungAbgeschlossen
  }
}
