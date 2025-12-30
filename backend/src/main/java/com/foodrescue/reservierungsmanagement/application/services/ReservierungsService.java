package com.foodrescue.reservierungsmanagement.application.services;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.shared.exception.DomainException;
import java.util.function.IntSupplier;

public class ReservierungsService {

  private final IntSupplier aktiveReservierungenFuerNutzer; // z.B. via Repository
  private final int maxProNutzer;

  public ReservierungsService(IntSupplier aktiveReservierungenFuerNutzer, int maxProNutzer) {
    this.aktiveReservierungenFuerNutzer = aktiveReservierungenFuerNutzer;
    this.maxProNutzer = maxProNutzer;
  }

  public void reserviere(Angebot angebot, String userId, Abholcode code) {
    if (aktiveReservierungenFuerNutzer.getAsInt() >= maxProNutzer) {
      throw new DomainException("Maximale Anzahl aktiver Reservierungen erreicht");
    }
    // Angebot reservieren - Event wird intern gespeichert
    angebot.reservieren(userId, code);
    // KEIN Return! Das Event wird automatisch vom Repository publiziert
    // und der Event Handler erstellt dann die Reservierung
  }
}
