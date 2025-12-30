package com.foodrescue.reservierungsmanagement.infrastructure.eventhandlers;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.events.AngebotReserviertEvent;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.reservierungsmanagement.infrastructure.repositories.ReservierungRepository;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event Handler für AngebotReserviertEvent.
 *
 * <p>Dieser Handler reagiert auf das Event "AngebotReserviert" aus dem Angebotsmanagement Bounded
 * Context und erstellt eine entsprechende Reservierung im Reservierungsmanagement Bounded Context.
 */
@Component
public class AngebotReserviertEventHandler {

  private final ReservierungRepository reservierungRepository;

  public AngebotReserviertEventHandler(ReservierungRepository reservierungRepository) {
    this.reservierungRepository = reservierungRepository;
  }

  /**
   * Behandelt das AngebotReserviertEvent.
   *
   * <p>Erstellt eine neue Reservierung basierend auf den Event-Daten.
   *
   * @param event Das empfangene Event
   */
  @EventListener
  public void handle(AngebotReserviertEvent event) {

    try {
      // Extrahiere Daten aus Event
      String angebotId = event.getAngebotId();
      String abholerId = event.getAbholerId();
      String abholcodeString = event.getAbholcode();

      // Erstelle Reservierungs-ID
      ReservierungsId reservierungsId = new ReservierungsId(UUID.randomUUID().toString());

      // Erstelle Abholcode Value Object
      Abholcode abholcode = Abholcode.random();

      // Erstelle Reservierung
      Reservierung reservierung =
          Reservierung.erstelle(reservierungsId, angebotId, abholerId, abholcode);

      // Speichere Reservierung
      reservierungRepository.speichern(reservierung);

    } catch (Exception e) {
      throw e;
    }
  }
}
