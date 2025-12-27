package com.foodrescue.abholungsmanagement.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.abholungsmanagement.application.commands.BestaetigeAbholungCommand;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.abholungsmanagement.domain.model.Abholung;
import com.foodrescue.abholungsmanagement.infrastructure.repositories.AbholungRepository;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.reservierungsmanagement.infrastructure.repositories.ReservierungRepository;
import com.foodrescue.shared.exception.DomainException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbholungApplicationServiceTest {

  @Mock AbholungRepository abholungRepository;
  @Mock ReservierungRepository reservierungRepository;

  @InjectMocks AbholungApplicationService service;

  @Captor ArgumentCaptor<Abholung> abholungCaptor;

  @Test
  void bestaetigeAbholung_happyPath_persistsAbholungAndUpdatesReservierung() {
    Reservierung reservierung = mock(Reservierung.class);
    when(reservierung.getId()).thenReturn("res-1");
    when(reservierung.getAbholcode()).thenReturn(Abholcode.of("AB12"));

    ReservierungsId reservierungsId = mock(ReservierungsId.class);
    when(reservierungsId.value()).thenReturn("res-1");

    when(reservierungRepository.findeMitId("res-1")).thenReturn(Optional.of(reservierung));

    BestaetigeAbholungCommand cmd =
        new BestaetigeAbholungCommand(reservierungsId, Abholcode.of("AB12"));

    service.bestaetigeAbholung(cmd);

    verify(abholungRepository).speichern(abholungCaptor.capture());
    Abholung gespeicherteAbholung = abholungCaptor.getValue();
    assertNotNull(gespeicherteAbholung.getId());
    assertEquals("res-1", gespeicherteAbholung.getReservierungsId());
    assertEquals(Abholung.Status.ABGESCHLOSSEN, gespeicherteAbholung.getStatus());

    verify(reservierung).bestaetigeAbholung(Abholcode.of("AB12"));
    verify(reservierungRepository).speichern(reservierung);

    verifyNoMoreInteractions(abholungRepository);
  }

  @Test
  void bestaetigeAbholung_reservierungNotFound_throwsAndDoesNotPersistAnything() {
    ReservierungsId reservierungsId = mock(ReservierungsId.class);
    when(reservierungsId.value()).thenReturn("missing");

    when(reservierungRepository.findeMitId("missing")).thenReturn(Optional.empty());

    BestaetigeAbholungCommand cmd =
        new BestaetigeAbholungCommand(reservierungsId, Abholcode.of("AB12"));

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> service.bestaetigeAbholung(cmd));
    assertEquals("Reservierung nicht gefunden", ex.getMessage());

    verify(abholungRepository, never()).speichern(any());
    verify(reservierungRepository, never()).speichern(any());
  }

  @Test
  void bestaetigeAbholung_wrongCode_propagatesDomainException_andDoesNotPersist() {
    Reservierung reservierung = mock(Reservierung.class);
    when(reservierung.getId()).thenReturn("res-1");
    when(reservierung.getAbholcode()).thenReturn(Abholcode.of("AB12"));

    ReservierungsId reservierungsId = mock(ReservierungsId.class);
    when(reservierungsId.value()).thenReturn("res-1");

    when(reservierungRepository.findeMitId("res-1")).thenReturn(Optional.of(reservierung));

    BestaetigeAbholungCommand cmd =
        new BestaetigeAbholungCommand(reservierungsId, Abholcode.of("CD34"));

    DomainException ex = assertThrows(DomainException.class, () -> service.bestaetigeAbholung(cmd));
    assertEquals("Falscher Abholcode", ex.getMessage());

    verify(abholungRepository, never()).speichern(any());
    verify(reservierung, never()).bestaetigeAbholung(any());
    verify(reservierungRepository, never()).speichern(any());
  }
}
