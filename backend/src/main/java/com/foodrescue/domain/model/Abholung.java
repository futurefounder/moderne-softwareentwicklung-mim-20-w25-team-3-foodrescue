package com.foodrescue.domain.model;

import java.time.LocalDateTime;

/**
 * Aggregate Root: Abholung
 *
 * <p>Repräsentiert den Abholvorgang einer reservierten Lebensmittelrettung.
 *
 * <p>Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
public class Abholung {

  private Long id;
  private Long reservierungId;
  private LocalDateTime abgeholtAm;

  // Konstruktoren, Getter, Setter und Business-Logik werden bei Bedarf hinzugefügt
  // wenn konkrete Use Cases diese benötigen

  private Abholung() {
    // Private Konstruktor für Persistence Framework
  }
}
