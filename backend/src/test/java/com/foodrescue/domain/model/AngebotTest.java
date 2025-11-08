package com.foodrescue.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test-Suite für die Validierungslogik des Angebot-Aggregates
 *
 * <p>Diese Tests wurden mit LLM-Unterstützung generiert und folgen dem TDD-Ansatz. Sie decken Happy
 * Path, Edge Cases und negative Testfälle ab.
 *
 * <p>HINWEIS: Diese Tests werden zunächst FEHLSCHLAGEN (Red-Phase), da die Validierung noch nicht
 * implementiert ist.
 */
public class AngebotTest {

  // ==============================================
  // HAPPY PATH TESTS - Gültige Eingaben
  // ==============================================

  @Test
  void sollteAngebotMitGültigenDatenErstellen() {
    // Arrange
    String name = "Max Mustermann";
    String email = "max.mustermann@example.com";
    String telefon = "+49 30 12345678";
    String beschreibung = "Frisches Gemüse vom Markt, noch gut haltbar bis morgen";
    String menge = "5 kg";

    // Act
    Angebot angebot = Angebot.erstellen(name, email, telefon, beschreibung, menge);

    // Assert
    assertNotNull(angebot);
    assertEquals(name, angebot.getAnbieterName());
    assertEquals(email, angebot.getAnbieterEmail());
    assertEquals(telefon, angebot.getAnbieterTelefon());
    assertEquals(beschreibung, angebot.getBeschreibung());
    assertEquals(menge, angebot.getMenge());
    assertNotNull(angebot.getErstelltAm());
  }

  @Test
  void sollteAngebotMitUmlautenImNamenErstellen() {
    // Test mit deutschen Umlauten
    Angebot angebot =
        Angebot.erstellen(
            "Müller-Lüdenscheidt",
            "mueller@example.de",
            "030 987654",
            "Verschiedene Backwaren vom Vortag",
            "10 Stück");

    assertNotNull(angebot);
    assertEquals("Müller-Lüdenscheidt", angebot.getAnbieterName());
  }

  @Test
  void sollteAngebotMitMehrteiligemNamenErstellen() {
    // Test mit Leerzeichen und Bindestrichen im Namen
    Angebot angebot =
        Angebot.erstellen(
            "Anna-Maria von der Heide",
            "anna@example.com",
            "0176 12345678",
            "Bio-Obst aus eigenem Anbau, leichte Druckstellen",
            "3.5 kg");

    assertNotNull(angebot);
  }

  // ==============================================
  // EMAIL VALIDATION TESTS
  // ==============================================

  @Test
  void sollteGültigeEmailsAkzeptieren() {
    // Standard-Email
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));

    // Email mit Zahlen und Punkten
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "user.123@test-domain.de",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));

    // Email mit Plus-Zeichen
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "user+tag@example.org",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigeEmailAbweisen_OhneAtZeichen() {
    // Email ohne @
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                Angebot.erstellen(
                    "Test User",
                    "invalidemail.com",
                    "030 123456",
                    "Test Beschreibung für Validierung",
                    "1 kg"));

    assertTrue(
        exception.getMessage().contains("Email")
            || exception.getMessage().contains("email")
            || exception.getMessage().contains("ungültig"));
  }

  @Test
  void sollteUngültigeEmailAbweisen_OhneDomain() {
    // Email ohne Domain
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "user@", "030 123456", "Test Beschreibung für Validierung", "1 kg"));
  }

  @Test
  void sollteUngültigeEmailAbweisen_OhneTLD() {
    // Email ohne Top-Level-Domain
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "user@domain",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigeEmailAbweisen_MitLeerzeichen() {
    // Email mit Leerzeichen (redundanter Test - zu spezifisch)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "user name@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigeEmailAbweisen_MehrereAtZeichen() {
    // Email mit mehreren @ (redundanter Test - ähnlich zu anderen Email-Tests)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "user@@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteLeereEmailAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "", "030 123456", "Test Beschreibung für Validierung", "1 kg"));
  }

  @Test
  void sollteNullEmailAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", null, "030 123456", "Test Beschreibung für Validierung", "1 kg"));
  }

  // ==============================================
  // NAME VALIDATION TESTS
  // ==============================================

  @Test
  void sollteGültigeNamenAkzeptieren() {
    // Einfacher Name
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Schmidt",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));

    // Name mit Bindestrich
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Müller-Schmidt",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));

    // Name mit Leerzeichen
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Anna Maria",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigenNamenAbweisen_MitZahlen() {
    // Name mit Zahlen
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Max123",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigenNamenAbweisen_MitSonderzeichen() {
    // Name mit ungültigen Sonderzeichen
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Max@Mustermann",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigenNamenAbweisen_ZuKurz() {
    // Name zu kurz (nur 1 Zeichen)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "M",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteNamenMitExaktzweiZeichenAkzeptieren() {
    // Edge Case: Genau 2 Zeichen (Minimum)
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Li",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteLeerenNamenAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteNullNamenAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                null,
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  // ==============================================
  // TELEFONNUMMER VALIDATION TESTS
  // ==============================================

  @Test
  void sollteGültigeTelefonnummernAkzeptieren() {
    // Deutsche Mobilnummer mit +49
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "+49 176 12345678",
                "Test Beschreibung für Validierung",
                "1 kg"));

    // Deutsche Festnetznummer mit 0
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "1 kg"));

    // Nummer mit Bindestrichen
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "0176-1234-5678",
                "Test Beschreibung für Validierung",
                "1 kg"));

    // Nummer mit Schrägstrichen
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030/123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigeTelefonnummerAbweisen_OhneVorwahl() {
    // Nummer ohne Vorwahl (startet nicht mit +49 oder 0)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "12345678",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigeTelefonnummerAbweisen_ZuKurz() {
    // Nummer zu kurz (weniger als Mindestlänge)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigeTelefonnummerAbweisen_MitBuchstaben() {
    // Nummer mit Buchstaben
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 ABC DEF",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteUngültigeTelefonnummerAbweisen_MitNullNachVorwahl() {
    // Ungültige Nummer: 0 direkt nach Vorwahl (redundanter Test)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "00123456",
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  @Test
  void sollteLeereTelefonnummerAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "", "Test Beschreibung für Validierung", "1 kg"));
  }

  @Test
  void sollteNullTelefonnummerAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                null,
                "Test Beschreibung für Validierung",
                "1 kg"));
  }

  // ==============================================
  // BESCHREIBUNG VALIDATION TESTS
  // ==============================================

  @Test
  void sollteGültigeBeschreibungenAkzeptieren() {
    // Beschreibung mit Mindestlänge (10 Zeichen)
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", "Zehn Chars", "1 kg"));

    // Beschreibung mit normaler Länge
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Dies ist eine ausführliche Beschreibung des Lebensmittelangebots",
                "1 kg"));

    // Beschreibung mit Sonderzeichen und Zahlen
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "5 Äpfel & 3 Birnen - frisch!",
                "1 kg"));
  }

  @Test
  void sollteUngültigeBeschreibungAbweisen_ZuKurz() {
    // Beschreibung unter 10 Zeichen
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", "Zu kurz", "1 kg"));
  }

  @Test
  void sollteUngültigeBeschreibungAbweisen_ZuLang() {
    // Beschreibung über 500 Zeichen
    String langeBeschreibung = "A".repeat(501);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", langeBeschreibung, "1 kg"));
  }

  @Test
  void sollteMaximalLängeBeschreibungAkzeptieren() {
    // Edge Case: Exakt 500 Zeichen (Maximum)
    String maxBeschreibung = "A".repeat(500);
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", maxBeschreibung, "1 kg"));
  }

  @Test
  void sollteUngültigeBeschreibungAbweisen_NurWhitespace() {
    // Beschreibung mit nur Leerzeichen
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", "          ", "1 kg"));
  }

  @Test
  void sollteLeereBeschreibungAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen("Test User", "test@example.com", "030 123456", "", "1 kg"));
  }

  @Test
  void sollteNullBeschreibungAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen("Test User", "test@example.com", "030 123456", null, "1 kg"));
  }

  // ==============================================
  // MENGE VALIDATION TESTS
  // ==============================================

  @Test
  void sollteGültigeMengenAkzeptieren() {
    // Menge mit Kilogramm
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "5 kg"));

    // Menge mit Gramm
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "500 g"));

    // Menge mit Stück
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "10 Stück"));

    // Dezimalzahl mit Einheit
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "2.5 kg"));

    // Nur Zahl ohne Einheit
    assertDoesNotThrow(
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "10"));
  }

  @Test
  void sollteUngültigeMengeAbweisen_MitBuchstabenAmAnfang() {
    // Menge beginnt mit Buchstaben statt Zahl
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "ungültig"));
  }

  @Test
  void sollteUngültigeMengeAbweisen_NegativeZahl() {
    // Negative Menge (unsinniger Test - zu spezifisch für Regex)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "-5 kg"));
  }

  @Test
  void sollteUngültigeMengeAbweisen_NurEinheit() {
    // Nur Einheit ohne Zahl
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "kg"));
  }

  @Test
  void sollteLeereMengeAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", "Test Beschreibung", ""));
  }

  @Test
  void sollteNullMengeAbweisen() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", "Test Beschreibung", null));
  }

  @Test
  void sollteMengeNullKgAbweisen() {
    // Edge Case: 0 kg sollte ungültig sein (redundant - Business-Logik, nicht Regex)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User",
                "test@example.com",
                "030 123456",
                "Test Beschreibung für Validierung",
                "0 kg"));
  }

  // ==============================================
  // KOMBINIERTE EDGE CASES
  // ==============================================

  @Test
  void sollteAlleFelder_AlsNullAbweisen() {
    // Alle Felder null (redundanter Test)
    assertThrows(IllegalArgumentException.class, () -> Angebot.erstellen(null, null, null, null, null));
  }

  @Test
  void sollteAlleFelder_AlsLeerAbweisen() {
    // Alle Felder leer (redundanter Test)
    assertThrows(IllegalArgumentException.class, () -> Angebot.erstellen("", "", "", "", ""));
  }
}

