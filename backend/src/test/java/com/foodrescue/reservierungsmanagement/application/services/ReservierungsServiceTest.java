package com.foodrescue.reservierungsmanagement.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.events.AngebotReserviertEvent;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

class ReservierungsServiceTest {

  @Test
  void reserviere_whenMaxReached_throwsDomainException_andDoesNotReserve() {
    Angebot angebot = mock(Angebot.class);
    ReservierungsService service = new ReservierungsService(() -> 3, 3);

    DomainException ex =
        assertThrows(
            DomainException.class,
            () ->
                service.reserviere(
                    angebot, "u1", Abholcode.of("AB12")) // GEÄNDERT: Abholcode.of -> new Abholcode
            );

    assertEquals("Maximale Anzahl aktiver Reservierungen erreicht", ex.getMessage());
    verifyNoInteractions(angebot);
  }

  @Test
  void reserviere_whenBelowMax_delegatesToAngebotReservieren() {
    Angebot angebot = mock(Angebot.class);

    // GEÄNDERT: Mock gibt jetzt Event zurück statt Reservierung
    AngebotReserviertEvent mockEvent = mock(AngebotReserviertEvent.class);
    when(angebot.reservieren(eq("u1"), any(Abholcode.class))).thenReturn(mockEvent);

    ReservierungsService service = new ReservierungsService(() -> 2, 3);

    // GEÄNDERT: reserviere() gibt jetzt void zurück
    service.reserviere(angebot, "u1", Abholcode.of("AB12"));

    // GEÄNDERT: Verify mit neuem Abholcode
    verify(angebot).reservieren(eq("u1"), any(Abholcode.class));
  }

  @Test
  void reserviere_whenBelowMax_doesNotThrow() {
    Angebot angebot = mock(Angebot.class);
    AngebotReserviertEvent mockEvent = mock(AngebotReserviertEvent.class);
    when(angebot.reservieren(anyString(), any(Abholcode.class))).thenReturn(mockEvent);

    ReservierungsService service = new ReservierungsService(() -> 0, 3);

    // Sollte nicht werfen
    assertDoesNotThrow(() -> service.reserviere(angebot, "u1", Abholcode.of("AB12")));
  }
}
