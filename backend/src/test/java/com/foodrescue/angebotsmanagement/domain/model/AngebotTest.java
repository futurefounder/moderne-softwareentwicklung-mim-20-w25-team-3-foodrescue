package com.foodrescue.angebotsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.events.AngebotErstelltEvent;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AngebotTest {

  private static Angebot newEntwurf() {
    AngebotsId id = AngebotsId.of("a1");
    UserId anbieter = new UserId(UUID.randomUUID());
    AbholZeitfenster fenster =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0));
    return Angebot.erstelle(id, anbieter, "Titel", "Beschreibung", Set.of("x"), fenster);
  }

  @Test
  void erstelle_createsEntwurfAndAddsCreatedEvent() {
    Angebot angebot = newEntwurf();

    assertEquals("a1", angebot.getId());
    assertEquals(Angebot.Status.ENTWURF, angebot.getStatus());
    assertEquals("Titel", angebot.getTitel());
    assertEquals("Beschreibung", angebot.getBeschreibung());
    assertEquals(Set.of("x"), angebot.getTags());
    assertNotNull(angebot.getZeitfenster());

    // Factory adds an event to internal list
    assertFalse(angebot.getDomainEvents().isEmpty());
    assertTrue(angebot.getDomainEvents().get(0) instanceof AngebotErstelltEvent);
  }

  @Test
  void veroeffentlichen_happyPath_setsStatusVerfuegbar_andReturnsEvent() {
    Angebot angebot = newEntwurf();

    List<DomainEvent> events = angebot.veroeffentlichen();

    assertEquals(Angebot.Status.VERFUEGBAR, angebot.getStatus());
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof AngebotErstelltEvent);

    AngebotErstelltEvent evt = (AngebotErstelltEvent) events.get(0);
    assertEquals("a1", evt.getAngebotsId());
  }

  @Test
  void veroeffentlichen_whenNotEntwurf_throws() {
    Angebot angebot = newEntwurf();
    angebot.veroeffentlichen();

    DomainException ex = assertThrows(DomainException.class, angebot::veroeffentlichen);
    assertEquals("Angebot ist bereits veröffentlicht oder aktiv", ex.getMessage());
  }

  @Test
  void reservieren_requiresStatusVerfuegbar() {
    Angebot angebot = newEntwurf(); // ENTWURF

    DomainException ex =
        assertThrows(
            DomainException.class, () -> angebot.reservieren("abholer-1", Abholcode.of("AB12")));
    assertEquals("Angebot ist nicht verfügbar", ex.getMessage());
  }

  @Test
  void reservieren_happyPath_setsStatusReserviert_andReturnsReservierung() {
    Angebot angebot = newEntwurf();
    angebot.veroeffentlichen();

    var reservierung = angebot.reservieren("abholer-1", Abholcode.of("AB12"));

    assertNotNull(reservierung);
    assertEquals(Angebot.Status.RESERVIERT, angebot.getStatus());
  }

  @Test
  void aktualisiere_allowedInEntwurfAndVerfuegbar_only() {
    Angebot angebot = newEntwurf();
    AbholZeitfenster fenster2 =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 1, 2, 10, 0), LocalDateTime.of(2025, 1, 2, 11, 0));

    assertDoesNotThrow(() -> angebot.aktualisiere("Neu", "NeuB", Set.of("y"), fenster2));
    assertEquals("Neu", angebot.getTitel());
    assertEquals(Set.of("y"), angebot.getTags());
    assertEquals(fenster2, angebot.getZeitfenster());

    // Now in VERFUEGBAR also allowed
    Angebot angebot2 = newEntwurf();
    angebot2.veroeffentlichen();
    assertDoesNotThrow(() -> angebot2.aktualisiere("T2", "B2", Set.of(), fenster2));
  }

  @Test
  void aktualisiere_notAllowedInReserviert() {
    Angebot angebot = newEntwurf();
    angebot.veroeffentlichen();
    angebot.reservieren("abholer-1", Abholcode.of("AB12"));

    DomainException ex =
        assertThrows(
            DomainException.class,
            () -> angebot.aktualisiere("x", "y", Set.of("t"), angebot.getZeitfenster()));
    assertEquals("Angebot kann nur im Entwurf oder verfügbar aktualisiert werden", ex.getMessage());
  }
}
