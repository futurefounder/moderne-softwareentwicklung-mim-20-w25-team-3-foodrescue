package com.foodrescue.reservierungsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.events.AbholungAbgeschlossen;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.events.ReservierungErstellt;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

public class ReservierungTest {

  @Test
  void erstellen_emittiertEvent() {
    var r = Reservierung.erstelle("r1", "a1", "user1", Abholcode.of("AB12"));

    assertEquals(Reservierung.Status.AKTIV, r.getStatus());
    assertTrue(r.getDomainEvents().stream().anyMatch(e -> e instanceof ReservierungErstellt));
  }

    @Test
    void getId_gibtUrspruenglicheReservierungsIdZurueck() {
        // Arrange: Eine Reservierung mit bekannter ID anlegen
        var reservierung =
                Reservierung.erstelle("reserv-99", "angebot-1", "user-1", Abholcode.of("AB12"));

        // Act: ID auslesen
        var id = reservierung.getId();

        // Assert: Die ID stimmt exakt mit der übergebenen überein
        assertEquals("reserv-99", id);
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

    @Test
    void createsValidId() {
      ReservierungsId id = ReservierungsId.of("res-99");

      assertEquals("res-99", id.value());
      assertEquals("res-99", id.toString());
    }

    @Test
    void equalIdsAreEqualAndHaveSameHashCode() {
      ReservierungsId id1 = ReservierungsId.of("res-99");
      ReservierungsId id2 = ReservierungsId.of("res-99");
      ReservierungsId idOther = ReservierungsId.of("res-100");

      assertEquals(id1, id2);
      assertEquals(id1.hashCode(), id2.hashCode());
      assertNotEquals(id1, idOther);
      assertNotEquals(null, id1);
      assertNotEquals("x", id1);
    }

    @Test
    void nullValueIsRejected() {
      assertThrows(DomainException.class, () -> ReservierungsId.of(null));
    }

    @Test
    void blankValueIsRejected() {
      assertThrows(DomainException.class, () -> ReservierungsId.of("   "));
    }

}
