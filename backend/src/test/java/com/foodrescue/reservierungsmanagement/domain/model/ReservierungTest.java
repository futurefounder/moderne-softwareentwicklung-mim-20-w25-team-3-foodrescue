package com.foodrescue.reservierungsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.events.AbholungAbgeschlossen;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.events.ReservierungErstellt;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReservierungTest {

  @Test
  void erstelle_emitsReservierungErstelltEvent_andInitialStatusAktiv() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle("r1", "a1", "u1", code);

    assertEquals("r1", r.getId());
    assertEquals("a1", r.getAngebotId());
    assertEquals("u1", r.getAbholerId());
    assertEquals(Reservierung.Status.AKTIV, r.getStatus());
    assertEquals(code, r.getAbholcode());

    List<DomainEvent> events = r.getDomainEvents();
    assertFalse(events.isEmpty());
    assertTrue(events.get(0) instanceof ReservierungErstellt);
    ReservierungErstellt created = (ReservierungErstellt) events.get(0);
    assertEquals("r1", created.getReservierungsId());
    assertNotNull(created.occurredOn());
  }

  @Test
  void erstelle_rejectsNulls() {
    Abholcode code = Abholcode.of("AB12");
    assertAll(
        () ->
            assertThrows(
                DomainException.class, () -> Reservierung.erstelle(null, "a1", "u1", code)),
        () ->
            assertThrows(
                DomainException.class, () -> Reservierung.erstelle("r1", null, "u1", code)),
        () ->
            assertThrows(
                DomainException.class, () -> Reservierung.erstelle("r1", "a1", null, code)),
        () ->
            assertThrows(
                DomainException.class, () -> Reservierung.erstelle("r1", "a1", "u1", null)));
  }

  @Test
  void bestaetigeAbholung_happyPath_setsStatusAbgeholt_andReturnsEvent() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle("r1", "a1", "u1", code);

    List<DomainEvent> events = r.bestaetigeAbholung(Abholcode.of("AB12"));

    assertEquals(Reservierung.Status.ABGEHOLT, r.getStatus());
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof AbholungAbgeschlossen);
    AbholungAbgeschlossen e = (AbholungAbgeschlossen) events.get(0);
    assertEquals("r1", e.getReservierungsId());
    assertNotNull(e.occurredOn());
  }

  @Test
  void bestaetigeAbholung_wrongCode_throws_andDoesNotChangeStatus() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle("r1", "a1", "u1", code);

    DomainException ex =
        assertThrows(DomainException.class, () -> r.bestaetigeAbholung(Abholcode.of("CD34")));
    assertEquals("Abholcode falsch", ex.getMessage());
    assertEquals(Reservierung.Status.AKTIV, r.getStatus());
  }

  @Test
  void bestaetigeAbholung_whenNotActive_throws() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle("r1", "a1", "u1", code);
    r.stornieren();

    DomainException ex = assertThrows(DomainException.class, () -> r.bestaetigeAbholung(code));
    assertEquals("Reservierung ist nicht aktiv", ex.getMessage());
  }

  @Test
  void stornieren_happyPath_setsStatusStorniert_andReturnsEvent() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle("r1", "a1", "u1", code);

    List<DomainEvent> events = r.stornieren();

    assertEquals(Reservierung.Status.STORNIERT, r.getStatus());
    assertEquals(1, events.size());
    assertNotNull(events.get(0));
    assertNotNull(events.get(0).occurredOn());

    // Ohne Compile-Time-Abhängigkeit auf evtl. nicht beigefügte Klasse
    assertEquals("ReservierungStorniertEvent", events.get(0).getClass().getSimpleName());
  }

  @Test
  void stornieren_whenNotActive_throws() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle("r1", "a1", "u1", code);
    r.bestaetigeAbholung(code);

    DomainException ex = assertThrows(DomainException.class, r::stornieren);
    assertEquals("Nur aktive Reservierungen können storniert werden", ex.getMessage());
  }
}
