package com.foodrescue.domain.model;

import java.time.LocalDateTime;

/**
 * Aggregate Root: Reservierung
 *
 * Repräsentiert eine Reservierung eines Lebensmittelangebots durch einen Retter.
 *
 * Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
public class Reservierung {

  private Long id;
  private Long angebotId;
  private LocalDateTime reserviertAm;

  // Konstruktoren, Getter, Setter und Business-Logik werden bei Bedarf hinzugefügt
  // wenn konkrete Use Cases diese benötigen

  private Reservierung() {
    // Private Konstruktor für Persistence Framework
  }
}
