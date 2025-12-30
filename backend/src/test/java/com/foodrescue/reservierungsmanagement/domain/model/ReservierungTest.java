package com.foodrescue.reservierungsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.events.AbholungAbgeschlossen;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.events.ReservierungErstellt;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReservierungTest {

  @Test
  void erstelle_emitsReservierungErstelltEvent_andInitialStatusAktiv() {
    // GEÄNDERT: Abholcode.of() statt Abholcode.of()
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

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
    // GEÄNDERT: Abholcode.of() statt Abholcode.of()
    Abholcode code = Abholcode.of("AB12");

    // GEÄNDERT: NullPointerException statt DomainException erwartet
    assertAll(
        () ->
            assertThrows(
                NullPointerException.class,
                () -> Reservierung.erstelle(null, "a1", "u1", code),
                "ReservierungsId darf nicht null sein"),
        () ->
            assertThrows(
                NullPointerException.class,
                () -> Reservierung.erstelle(new ReservierungsId("r1"), null, "u1", code),
                "AngebotId darf nicht null sein"),
        () ->
            assertThrows(
                NullPointerException.class,
                () -> Reservierung.erstelle(new ReservierungsId("r1"), "a1", null, code),
                "AbholerId darf nicht null sein"),
        () ->
            assertThrows(
                NullPointerException.class,
                () -> Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", null),
                "Abholcode darf nicht null sein"));
  }

  @Test
  void bestaetigeAbholung_happyPath_setsStatusAbgeholt_andReturnsEvent() {
    // GEÄNDERT: Abholcode.of() statt Abholcode.of()
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

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
    // GEÄNDERT: Abholcode.of() statt Abholcode.of()
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

    // GEÄNDERT: Fehlermeldung angepasst an die verbesserte Implementierung
    DomainException ex =
        assertThrows(DomainException.class, () -> r.bestaetigeAbholung(Abholcode.of("CD34")));
    assertEquals("Abholcode ist ungültig", ex.getMessage());
    assertEquals(Reservierung.Status.AKTIV, r.getStatus());
  }

  @Test
  void bestaetigeAbholung_whenNotActive_throws() {
    // GEÄNDERT: Abholcode.of() statt Abholcode.of()
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);
    r.stornieren();

    // GEÄNDERT: Fehlermeldung enthält jetzt den Status
    DomainException ex = assertThrows(DomainException.class, () -> r.bestaetigeAbholung(code));
    assertEquals("Reservierung ist nicht aktiv (Status: STORNIERT)", ex.getMessage());
  }

  @Test
  void stornieren_happyPath_setsStatusStorniert_andReturnsEvent() {
    // GEÄNDERT: Abholcode.of() statt Abholcode.of()
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

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
    // GEÄNDERT: Abholcode.of() statt Abholcode.of()
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);
    r.bestaetigeAbholung(code);

    // GEÄNDERT: Fehlermeldung enthält jetzt den Status
    DomainException ex = assertThrows(DomainException.class, r::stornieren);
    assertEquals(
        "Nur aktive Reservierungen können storniert werden (Status: ABGEHOLT)", ex.getMessage());
  }

  // NEUE TESTS für die zusätzlichen Domain-Methoden

  @Test
  void istAktiv_returnsTrueForNewReservierung() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

    assertTrue(r.istAktiv());
    assertFalse(r.istAbgeschlossen());
    assertFalse(r.istStorniert());
  }

  @Test
  void istAbgeschlossen_returnsTrueAfterAbholung() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);
    r.bestaetigeAbholung(code);

    assertFalse(r.istAktiv());
    assertTrue(r.istAbgeschlossen());
    assertFalse(r.istStorniert());
  }

  @Test
  void istStorniert_returnsTrueAfterStornierung() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);
    r.stornieren();

    assertFalse(r.istAktiv());
    assertFalse(r.istAbgeschlossen());
    assertTrue(r.istStorniert());
  }

  @Test
  void codeIstKorrekt_checksAbholcode() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

    assertTrue(r.codeIstKorrekt(Abholcode.of("AB12")));
    assertFalse(r.codeIstKorrekt(Abholcode.of("CD34")));
  }

  @Test
  void reservierungsdauer_returnsPositiveDuration() throws InterruptedException {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

    Thread.sleep(10); // Kurz warten

    var dauer = r.reservierungsdauer();
    assertNotNull(dauer);
    assertTrue(dauer.toMillis() >= 10);
  }

  @Test
  void clearDomainEvents_removesAllEvents() {
    Abholcode code = Abholcode.of("AB12");
    Reservierung r = Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", code);

    assertFalse(r.getDomainEvents().isEmpty());

    r.clearDomainEvents();

    assertTrue(r.getDomainEvents().isEmpty());
  }
}
