package com.foodrescue.reservierungsmanagement.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

class ReservierungsServiceTest {

  @Test
  void reserviere_whenMaxReached_throwsDomainException_andDoesNotReserve() {
    Angebot angebot = mock(Angebot.class);
    ReservierungsService service = new ReservierungsService(() -> 3, 3);

    DomainException ex =
        assertThrows(
            DomainException.class, () -> service.reserviere(angebot, "u1", Abholcode.of("AB12")));

    assertEquals("Maximale Anzahl aktiver Reservierungen erreicht", ex.getMessage());
    verifyNoInteractions(angebot);
  }

  @Test
  void reserviere_whenBelowMax_delegatesToAngebotReservieren() {
    Angebot angebot = mock(Angebot.class);
    Reservierung reservierung = mock(Reservierung.class);
    when(angebot.reservieren(eq("u1"), any())).thenReturn(reservierung);

    ReservierungsService service = new ReservierungsService(() -> 2, 3);

    Reservierung result = service.reserviere(angebot, "u1", Abholcode.of("AB12"));

    assertSame(reservierung, result);
    verify(angebot).reservieren(eq("u1"), eq(Abholcode.of("AB12")));
  }
}
