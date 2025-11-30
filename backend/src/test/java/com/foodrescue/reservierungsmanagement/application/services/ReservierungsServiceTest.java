package com.foodrescue.reservierungsmanagement.application.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.reservierungsmanagement.application.services.ReservierungsService;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.shared.exception.DomainException;
import org.junit.jupiter.api.Test;

public class ReservierungsServiceTest {

  @Test
  void reserviert_wenn_unter_limit() {
    // Arrange
    Angebot angebot = mock(Angebot.class);
    Abholcode code = mock(Abholcode.class);
    Reservierung erwarteteReservierung = mock(Reservierung.class);

    when(angebot.reservieren("user-1", code)).thenReturn(erwarteteReservierung);

    // Nutzer hat aktuell 0 aktive Reservierungen, Limit = 3
    ReservierungsService service = new ReservierungsService(() -> 0, 3);

    // Act
    Reservierung result = service.reserviere(angebot, "user-1", code);

    // Assert
    assertThat(result).isSameAs(erwarteteReservierung);
    verify(angebot).reservieren("user-1", code); // delegiert richtig an Angebot
  }

  @Test
  void wirft_exception_wenn_limit_erreicht() {
    // Arrange
    Angebot angebot = mock(Angebot.class);
    Abholcode code = mock(Abholcode.class);

    // Nutzer hat schon 3 Reservierungen, Limit = 3
    ReservierungsService service = new ReservierungsService(() -> 3, 3);

    // Act + Assert
    assertThatThrownBy(() -> service.reserviere(angebot, "user-1", code))
        .isInstanceOf(DomainException.class)
        .hasMessageContaining("Maximale Anzahl aktiver Reservierungen erreicht");

    // Es darf keine Reservierung mehr angelegt werden
    verifyNoInteractions(angebot);
  }
}
