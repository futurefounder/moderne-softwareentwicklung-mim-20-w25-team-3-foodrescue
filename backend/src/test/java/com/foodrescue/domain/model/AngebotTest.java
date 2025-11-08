package com.foodrescue.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test-Suite für die Validierungslogik des Angebot-Aggregates
 *
 * Diese Tests wurden mit LLM-Unterstützung generiert und folgen dem TDD-Ansatz. Sie decken Happy
 * Path, Edge Cases und negative Testfälle ab.
 *
 * HINWEIS: Diese Tests werden zunächst FEHLSCHLAGEN (Red-Phase), da die Validierung noch nicht
 * implementiert ist.
 *
 * Übersicht der Teststruktur:
 * 
 * 1. Happy Path Tests:
 *    - Überprüfen die grundlegende Objekterstellung und Getter-Funktionalität ohne Validierung
 *    - Diese Tests BESTEHEN in der RED-Phase, da sie keine Validierung erwarten
 *    - Verwenden assertNotNull und assertEquals
 *
 * 2. Edge Case Tests:
 *    - Testen Grenzwerte (z.B. minimale Namenslänge, maximale Beschreibungslänge)
 *    - Diese Tests SCHLAGEN FEHL in der RED-Phase, da die Validierung noch nicht implementiert ist
 *    - Verwenden assertThrows, um zu prüfen, ob bei Grenzwerten Exceptions geworfen werden
 *
 * 3. Negative Tests:
 *    - Überprüfen, ob ungültige Eingaben (z.B. falsch formatierte E-Mail, null-Werte) abgelehnt werden
 *    - Diese Tests SCHLAGEN FEHL in der RED-Phase, da die Validierung noch nicht implementiert ist
 *    - Verwenden assertThrows, um zu prüfen, ob bei ungültigen Eingaben IllegalArgumentException geworfen wird
 */
public class AngebotTest {

  // ==============================================
  // HAPPY PATH TESTS 
  // ==============================================

  @Test
  void sollteAngebotMitGültigenDatenErstellen() {
    // Standard Happy Path: Alle Felder gültig
    String name = "Max Mustermann";
    String email = "max.mustermann@example.com";
    String telefon = "+49 30 12345678";
    String beschreibung = "Frisches Gemüse vom Markt, noch gut haltbar bis morgen";
    String menge = "5 kg";

    Angebot angebot = Angebot.erstellen(name, email, telefon, beschreibung, menge);

    assertNotNull(angebot);
    assertEquals(name, angebot.getAnbieterName());
    assertEquals(email, angebot.getAnbieterEmail());
    assertEquals(telefon, angebot.getAnbieterTelefon());
    assertEquals(beschreibung, angebot.getBeschreibung());
    assertEquals(menge, angebot.getMenge());
    assertNotNull(angebot.getErstelltAm());
  }


  // ==============================================
  // EDGE CASE TESTS 
  // ==============================================

  @Test
  void sollteUngültigenNamenAbweisen_ZuKurz() {
    // Edge Case: Name zu kurz (unter Mindestlänge von 2 Zeichen)
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
  void sollteUngültigeBeschreibungAbweisen_ZuLang() {
    // Edge Case: Beschreibung zu lang (über Maximallänge von 500 Zeichen)
    String langeBeschreibung = "A".repeat(501);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                "Test User", "test@example.com", "030 123456", langeBeschreibung, "1 kg"));
  }

  // ==============================================
  // NEGATIVE TESTS 
  // ==============================================

  @Test
  void sollteUngültigeEmailAbweisen() {
    // Negative Test: Email ohne @ (grundlegender Formatfehler)
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
  void sollteNullWerteAbweisen() {
    // Negative Test: Null-Wert (redundanter Test - wird mehrfach geprüft)
    assertThrows(
        IllegalArgumentException.class,
        () ->
            Angebot.erstellen(
                null, "test@example.com", "030 123456", "Test Beschreibung", "1 kg"));
  }
}

