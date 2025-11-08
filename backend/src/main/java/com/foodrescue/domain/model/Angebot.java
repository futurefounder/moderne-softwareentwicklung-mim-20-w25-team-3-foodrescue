package com.foodrescue.domain.model;

import java.time.LocalDateTime;

/**
 * Aggregate Root: Angebot
 *
 * Repräsentiert ein Lebensmittelangebot, das von einem Anbieter zur Rettung bereitgestellt wird.
 *
 * Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
public class Angebot {

  private Long id;
  private String anbieterName;
  private String anbieterEmail;
  private String anbieterTelefon;
  private String beschreibung;
  private String menge; // z.B. "2.5 kg" oder "10 Stück"
  private LocalDateTime erstelltAm;

  // Private Konstruktor für Persistence Framework
  private Angebot() {}

  // Private Konstruktor für Factory-Methode
  private Angebot(
      String anbieterName,
      String anbieterEmail,
      String anbieterTelefon,
      String beschreibung,
      String menge) {
    this.anbieterName = anbieterName;
    this.anbieterEmail = anbieterEmail;
    this.anbieterTelefon = anbieterTelefon;
    this.beschreibung = beschreibung;
    this.menge = menge;
    this.erstelltAm = LocalDateTime.now();
  }

  /**
   * Factory-Methode zum Erstellen eines neuen Angebots
   * 
   * HINWEIS: Validierung wird in TDD Schritt 2 hinzugefügt
   */
  public static Angebot erstellen(
      String anbieterName,
      String anbieterEmail,
      String anbieterTelefon,
      String beschreibung,
      String menge) {
    // TODO: Validierung wird nach TDD-Prinzip in Schritt 2 implementiert
    // Aktuell keine Validierung - Tests werden fehlschlagen (Red-Phase)
    return new Angebot(anbieterName, anbieterEmail, anbieterTelefon, beschreibung, menge);
  }

  // Getter-Methoden
  public Long getId() {
    return id;
  }

  public String getAnbieterName() {
    return anbieterName;
  }

  public String getAnbieterEmail() {
    return anbieterEmail;
  }

  public String getAnbieterTelefon() {
    return anbieterTelefon;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public String getMenge() {
    return menge;
  }

  public LocalDateTime getErstelltAm() {
    return erstelltAm;
  }

  // Geplante Regex-Patterns für zukünftige Validierung (TDD Schritt 2):
  //
  // EMAIL_PATTERN: ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$
  // - Lokalteil mit Buchstaben, Zahlen, +, _, ., -
  // - @ Symbol
  // - Domain mit Buchstaben, Zahlen, ., -
  // - TLD mit mindestens 2 Buchstaben
  //
  // NAME_PATTERN: ^[A-Za-zÄÖÜäöüß]+([ -][A-Za-zÄÖÜäöüß]+)*$
  // - Mindestens 2 Zeichen
  // - Buchstaben (inkl. Umlaute)
  // - Leerzeichen und Bindestriche erlaubt (nicht am Anfang/Ende)
  //
  // TELEFON_PATTERN: ^(\+49|0)[1-9][0-9\s\-/]{4,}$
  // - Starts mit +49 oder 0
  // - Gefolgt von Ziffer 1-9
  // - Mindestens 4 weitere Ziffern (mit optionalen Leerzeichen, -, /)
  //
  // BESCHREIBUNG_PATTERN: min 10 Zeichen, max 500 Zeichen, nicht nur Whitespace
  // - Längenprüfung: 10 <= length <= 500
  // - trim().length() > 0
  //
  // MENGE_PATTERN: ^[0-9]+(\.[0-9]+)?\s*(kg|g|Stück|Stk\.?|Packungen?|Liter?|ml)?$
  // - Positive Zahl (mit optionalem Dezimalteil)
  // - Optional: Einheit (kg, g, Stück, Packung, Liter, ml)
}
