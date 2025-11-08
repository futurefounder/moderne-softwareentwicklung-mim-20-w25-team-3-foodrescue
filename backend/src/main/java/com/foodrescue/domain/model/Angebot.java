package com.foodrescue.domain.model;

import java.time.LocalDateTime;

/**
 * Aggregate Root: Angebot
 *
 * <p>Repräsentiert ein Lebensmittelangebot, das von einem Anbieter zur Rettung bereitgestellt wird.
 *
 * <p>Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
public class Angebot {

  private Long id;
  private String beschreibung;
  private LocalDateTime erstelltAm;

  // Konstruktoren, Getter, Setter und Business-Logik werden bei Bedarf hinzugefügt
  // wenn konkrete Use Cases diese benötigen

  private Angebot() {
    // Private Konstruktor für Persistence Framework
  }
}
