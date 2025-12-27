package com.foodrescue.reservierungsmanagement.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import com.foodrescue.reservierungsmanagement.application.commands.ReserviereAngebotCommand;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.reservierungsmanagement.infrastructure.repositories.ReservierungRepository;
import com.foodrescue.shared.exception.DomainException;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservierungsApplicationServiceTest {

  @Mock ReservierungRepository reservierungRepository;
  @Mock AngebotRepository angebotRepository;

  @InjectMocks ReservierungsApplicationService service;

  @Captor ArgumentCaptor<Reservierung> reservierungCaptor;
  @Captor ArgumentCaptor<Angebot> angebotCaptor;

  @Test
  void reserviereAngebot_happyPath_persistsReservierungAndUpdatesAngebot_andReturnsId() {
    UserId userId = new UserId(UUID.randomUUID());
    AngebotsId angebotsId = AngebotsId.of("a1");

    Angebot angebot =
        Angebot.erstelle(
            angebotsId,
            new UserId(UUID.randomUUID()),
            "Titel",
            "Beschreibung",
            java.util.Set.of("tag"),
            AbholZeitfenster.of(
                LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0)));
    angebot.veroeffentlichen();

    when(angebotRepository.findeMitId(eq(angebotsId))).thenReturn(Optional.of(angebot));
    when(reservierungRepository.findeFuerAbholer(anyString())).thenReturn(List.of());

    ReserviereAngebotCommand cmd = new ReserviereAngebotCommand(angebotsId, userId);

    ReservierungsId rid = service.reserviereAngebot(cmd);

    assertNotNull(rid);
    assertNotNull(rid.value());

    verify(reservierungRepository).speichern(reservierungCaptor.capture());
    Reservierung gespeicherteReservierung = reservierungCaptor.getValue();
    assertNotNull(gespeicherteReservierung);
    assertEquals("a1", gespeicherteReservierung.getAngebotId());
    assertEquals(userId.getValue().toString(), gespeicherteReservierung.getAbholerId());
    assertNotNull(gespeicherteReservierung.getAbholcode());

    verify(angebotRepository).speichern(angebotCaptor.capture());
    Angebot gespeichertesAngebot = angebotCaptor.getValue();
    assertSame(angebot, gespeichertesAngebot);
    assertEquals(Angebot.Status.RESERVIERT, angebot.getStatus());

    assertEquals(gespeicherteReservierung.getId(), rid.value());
  }

  @Test
  void reserviereAngebot_whenAngebotNotFound_throws_andDoesNotPersist() {
    AngebotsId angebotsId = AngebotsId.of("missing");
    UserId userId = new UserId(UUID.randomUUID());

    when(angebotRepository.findeMitId(eq(angebotsId))).thenReturn(Optional.empty());

    ReserviereAngebotCommand cmd = new ReserviereAngebotCommand(angebotsId, userId);

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> service.reserviereAngebot(cmd));
    assertEquals("Angebot nicht gefunden", ex.getMessage());

    verify(reservierungRepository, never()).speichern(any());
    verify(angebotRepository, never()).speichern(any());
  }

  @Test
  void reserviereAngebot_whenMaxActiveReached_throwsDomainException_andDoesNotPersist() {
    UserId userId = new UserId(UUID.randomUUID());
    AngebotsId angebotsId = AngebotsId.of("a1");

    Angebot angebot =
        Angebot.erstelle(
            angebotsId,
            new UserId(UUID.randomUUID()),
            "Titel",
            "Beschreibung",
            java.util.Set.of(),
            AbholZeitfenster.of(
                LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0)));
    angebot.veroeffentlichen();

    when(angebotRepository.findeMitId(eq(angebotsId))).thenReturn(Optional.of(angebot));
    when(reservierungRepository.findeFuerAbholer(anyString()))
        .thenReturn(
            List.of(mock(Reservierung.class), mock(Reservierung.class), mock(Reservierung.class)));

    ReserviereAngebotCommand cmd = new ReserviereAngebotCommand(angebotsId, userId);

    DomainException ex = assertThrows(DomainException.class, () -> service.reserviereAngebot(cmd));
    assertEquals("Maximale Anzahl aktiver Reservierungen erreicht", ex.getMessage());

    verify(reservierungRepository, never()).speichern(any());
    verify(angebotRepository, never()).speichern(any());
    assertEquals(Angebot.Status.VERFUEGBAR, angebot.getStatus());
  }

  @Test
  void findeGeplanteAbholungenFuerUser_whenAngebotMissing_mapsNullFields() {
    Reservierung r =
        Reservierung.erstelle(
            "r1", "a1", "u1", com.foodrescue.abholungsmanagement.domain.model.Abholcode.of("AB12"));
    when(reservierungRepository.findeFuerAbholer("u1")).thenReturn(List.of(r));
    when(angebotRepository.findeMitId(eq(new AngebotsId("a1")))).thenReturn(Optional.empty());

    var res = service.findeGeplanteAbholungenFuerUser("u1");

    assertEquals(1, res.size());
    var dto = res.get(0);
    assertEquals("r1", dto.reservierungId());
    assertEquals("a1", dto.angebotId());
    assertNull(dto.angebotTitel());
    assertNull(dto.angebotBeschreibung());
    assertEquals("AKTIV", dto.status());
    assertEquals("AB12", dto.abholcode());
    assertNull(dto.zeitfensterVon());
    assertNull(dto.zeitfensterBis());
  }

  @Test
  void findeGeplanteAbholungenFuerUser_whenAngebotAndZeitfensterPresent_formatsIsoLocalDateTime() {
    var angebot =
        Angebot.erstelle(
            AngebotsId.of("a1"),
            new UserId(UUID.randomUUID()),
            "T",
            "B",
            java.util.Set.of(),
            AbholZeitfenster.of(
                LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 12, 30)));

    Reservierung r =
        Reservierung.erstelle(
            "r1", "a1", "u1", com.foodrescue.abholungsmanagement.domain.model.Abholcode.of("AB12"));

    when(reservierungRepository.findeFuerAbholer("u1")).thenReturn(List.of(r));
    when(angebotRepository.findeMitId(eq(new AngebotsId("a1")))).thenReturn(Optional.of(angebot));

    var res = service.findeGeplanteAbholungenFuerUser("u1");

    assertEquals(1, res.size());
    var dto = res.get(0);
    assertEquals("T", dto.angebotTitel());
    assertEquals("B", dto.angebotBeschreibung());
    assertEquals("2025-01-01T10:00:00", dto.zeitfensterVon());
    assertEquals("2025-01-01T12:30:00", dto.zeitfensterBis());
  }
}
