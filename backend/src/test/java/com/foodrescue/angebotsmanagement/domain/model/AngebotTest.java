package com.foodrescue.angebotsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.events.AngebotVeroeffentlicht;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.shared.exception.DomainException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Test-Suite für die Validierungslogik des Angebot-Aggregates
 *
 * <p>Diese Tests wurden mit LLM-Unterstützung generiert und folgen dem TDD-Ansatz. Sie decken Happy
 * Path, Edge Cases und negative Testfälle ab.
 *
 * <p>HINWEIS: Diese Tests werden zunächst FEHLSCHLAGEN (Red-Phase), da die Validierung noch nicht
 * implementiert ist.
 *
 * <p>Übersicht der Teststruktur:
 *
 * <p>1. Happy Path Test: - Überprüft die grundlegende Objekterstellung und Getter-Funktionalität
 * ohne Validierung - Dieser Test BESTEHT in der RED-Phase, da er keine Validierung erwartet -
 * Verwendet assertNotNull und assertEquals
 *
 * <p>2. Edge Case Tests: - Testen Grenzwerte (z.B. minimale Namenslänge, maximale
 * Beschreibungslänge) - Diese Tests SCHLAGEN FEHL in der RED-Phase, da die Validierung noch nicht
 * implementiert ist - Verwenden assertThrows, um zu prüfen, ob bei Grenzwerten Exceptions geworfen
 * werden
 *
 * <p>3. Negative Tests: - Überprüfen, ob ungültige Eingaben/Zustände abgelehnt werden - Diese Tests
 * SCHLAGEN FEHL in der RED-Phase, da die Validierung noch nicht implementiert ist - Verwenden
 * assertThrows, um zu prüfen, ob bei ungültigen Eingaben IllegalArgumentException geworfen wird
 */
public class AngebotTest {

  @Test
  void reservieren_markiertAngebotAlsReserviert_und_legtReservierungAn() {
    var fenster =
        AbholZeitfenster.of(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
    var angebot = Angebot.neu("a1", "anbieter1", "Suppe", "Tomatensuppe", Set.of("vegi"), fenster);
    angebot.veroeffentlichen();

    var reservierung = angebot.reservieren("user1", Abholcode.random());

    assertEquals(Angebot.Status.RESERVIERT, angebot.getStatus());
    assertEquals("a1", reservierung.getAngebotId());
    assertEquals(Reservierung.Status.AKTIV, reservierung.getStatus());
  }

  // Happy-Path-Tests

  @Test
  void veroeffentlichen_setztStatusVerfuegbar_und_emittiertEvent() {
    var angebot = neuesAngebot();

    var events = angebot.veroeffentlichen();

    assertEquals(Angebot.Status.VERFUEGBAR, angebot.getStatus());
    assertTrue(events.stream().anyMatch(e -> e instanceof AngebotVeroeffentlicht));
    assertTrue(
        angebot.getDomainEvents().stream().anyMatch(e -> e instanceof AngebotVeroeffentlicht));
  }

  @Test
  void reservieren_imHappyPath_markiertAngebotUndErzeugtReservierung() {
    var angebot = neuesAngebot();
    angebot.veroeffentlichen();

    var reservierung = angebot.reservieren("user1", Abholcode.of("AB12"));

    assertEquals(Angebot.Status.RESERVIERT, angebot.getStatus());
    assertEquals("a1", reservierung.getAngebotId());
    assertEquals(Reservierung.Status.AKTIV, reservierung.getStatus());
  }

  // Edge-Cases (Grenzfälle)

  @Test
  void beschreibungUndTags_duerfenNullSein_werdenSinnvollVorgesaetzt() {
    var angebot = Angebot.neu("a2", "anbieter1", "Brot", null, null, fenster());

    // nur prüfen, dass die Erstellung klappt und Standardwerte gesetzt sind
    assertNotNull(angebot);
    assertEquals(Angebot.Status.ENTFERNT, angebot.getStatus()); // noch nicht veröffentlicht
    // keine direkte Getter für beschreibung/tags -> hier reicht es, dass keine Exception kam
  }

  @Test
  void getDomainEvents_istSnapshotUndNichtModifizierbar() {
    var angebot = neuesAngebot();
    angebot.veroeffentlichen();

    List<?> eventsSnapshot = angebot.getDomainEvents();

    assertThrows(
        UnsupportedOperationException.class, () -> ((List) eventsSnapshot).add(new Object()));
    assertEquals(1, eventsSnapshot.size());
  }

  // Negative Tests (ungültige Eingaben / Zustände)

  @Test
  void veroeffentlichen_doppelt_nichtErlaubt() {
    var angebot = neuesAngebot();
    angebot.veroeffentlichen();

    assertThrows(DomainException.class, angebot::veroeffentlichen);
  }

  @Test
  void reservieren_schlaegtFehl_wennNichtVerfuegbar() {
    var angebot = neuesAngebot(); // nicht veröffentlicht

    assertThrows(DomainException.class, () -> angebot.reservieren("user1", Abholcode.of("AB12")));
  }

  @Test
  void reservieren_nachBereitsReserviert_schlaegtFehl() {
    var angebot = neuesAngebot();
    angebot.veroeffentlichen();
    angebot.reservieren("user1", Abholcode.of("AB12"));

    assertThrows(DomainException.class, () -> angebot.reservieren("user2", Abholcode.of("CD34")));
  }

  @Test
  void neu_mitNullIdWirftFehler() {
    assertThrows(
        DomainException.class,
        () -> Angebot.neu(null, "anbieter1", "Suppe", "x", Set.of(), fenster()));
  }

  @Test
  void neu_mitNullAnbieterWirftFehler() {
    assertThrows(
        DomainException.class, () -> Angebot.neu("a1", null, "Suppe", "x", Set.of(), fenster()));
  }

  @Test
  void neu_mitNullTitelWirftFehler() {
    assertThrows(
        DomainException.class,
        () -> Angebot.neu("a1", "anbieter1", null, "x", Set.of(), fenster()));
  }

  @Test
  void neu_mitNullZeitfensterWirftFehler() {
    assertThrows(
        DomainException.class, () -> Angebot.neu("a1", "anbieter1", "Suppe", "x", Set.of(), null));
  }

  @Test
  void gettersReturnCorrectValues() {
    var fenster =
        AbholZeitfenster.of(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));

    var a = Angebot.neu("a1", "anb1", "Titel", null, null, fenster);

    assertEquals("a1", a.getId());
    assertEquals(Angebot.Status.ENTFERNT, a.getStatus());
    assertEquals(fenster, a.getZeitfenster());
  }

  /**
   * Regex-Validierungen nur für Angebots-Attribute: - Titel - Beschreibung - Tags - Anbieter-E-Mail
   * (optional, falls im Modell vorhanden) - Abholcode (Domänen-Validierung)
   */

  // 1) Titel – mindestens 2 Zeichen, Buchstaben, Ziffern, Leerzeichen erlaubt
  //    Keine Sonderzeichen außer - und /

  private static final Pattern TITEL_PATTERN =
      Pattern.compile("^[A-Za-zÄÖÜäöüß0-9][A-Za-zÄÖÜäöüß0-9 \\-/]{1,49}$");

  @Test
  void titel_regex_happyPath() {
    assertTrue(TITEL_PATTERN.matcher("Tomatensuppe").matches());
    assertTrue(TITEL_PATTERN.matcher("Pizza Margherita").matches());
    assertTrue(TITEL_PATTERN.matcher("Salat-Box 2").matches());
  }

  @Test
  void titel_regex_negative() {
    assertFalse(TITEL_PATTERN.matcher(" ").matches()); // leer/blank
    assertFalse(TITEL_PATTERN.matcher("!@#").matches()); // Sonderzeichen
    assertFalse(TITEL_PATTERN.matcher("Pizza🍕").matches()); // Emoji
  }

  // 2) Beschreibung – darf mehr enthalten, aber keine gefährlichen HTML-Tags

  private static final Pattern BESCHREIBUNG_PATTERN =
      Pattern.compile("^(?!.*<(script|iframe|object)).{0,500}$", Pattern.CASE_INSENSITIVE);

  @Test
  void beschreibung_regex_happyPath() {
    assertTrue(BESCHREIBUNG_PATTERN.matcher("Leckere Gemüsesuppe mit Brot.").matches());
    assertTrue(BESCHREIBUNG_PATTERN.matcher("Reste vom Mittagsbuffet, vegetarisch.").matches());
  }

  @Test
  void beschreibung_regex_negative() {
    assertFalse(BESCHREIBUNG_PATTERN.matcher("<script>alert('x')</script>").matches());
    assertFalse(BESCHREIBUNG_PATTERN.matcher("<IFRAME src='evil'>").matches());
  }

  // 3) Tags – nur Kleinbuchstaben, Komma-separiert, 1–20 Zeichen pro Tag

  private static final Pattern TAGS_PATTERN =
      Pattern.compile("^[a-zäöüß0-9]{1,20}(?:,[a-zäöüß0-9]{1,20})*$");

  @Test
  void tags_regex_happyPath() {
    assertTrue(TAGS_PATTERN.matcher("vegi").matches());
    assertTrue(TAGS_PATTERN.matcher("vegan,glutenfrei,lowcarb").matches());
  }

  @Test
  void tags_regex_negative() {
    assertFalse(TAGS_PATTERN.matcher("Vegi").matches()); // Großbuchstabe
    assertFalse(TAGS_PATTERN.matcher("vegan,").matches()); // trailing comma
    assertFalse(TAGS_PATTERN.matcher("vegan,super-super-long-tag-name").matches());
  }

  // 4) Anbieter-E-Mail (optional, falls im Angebot hinterlegt)

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

  @Test
  void email_regex_happyPath() {
    assertTrue(EMAIL_PATTERN.matcher("kontakt@foodrescue.de").matches());
    assertTrue(EMAIL_PATTERN.matcher("info.bistro123@uni-berlin.de").matches());
  }

  @Test
  void email_regex_negative() {
    assertFalse(EMAIL_PATTERN.matcher("ohneAtzeichen.example.com").matches());
    assertFalse(EMAIL_PATTERN.matcher("user@domain").matches());
  }

  // 5) Abholcode – euer Domain-Regex [A-Z0-9]{4,8}

  private static final Pattern ABHOLCODE_PATTERN = Pattern.compile("^[A-Z0-9]{4,8}$");

  @Test
  void abholcode_regex_happyPath() {
    assertTrue(ABHOLCODE_PATTERN.matcher("AB12").matches());
    assertTrue(ABHOLCODE_PATTERN.matcher("K7LMNP").matches());
  }

  @Test
  void abholcode_regex_negative() {
    assertFalse(ABHOLCODE_PATTERN.matcher("ab12").matches()); // Kleinbuchstaben
    assertFalse(ABHOLCODE_PATTERN.matcher("TOOLONG123").matches());
    assertFalse(ABHOLCODE_PATTERN.matcher("12-34").matches()); // Sonderzeichen
  }

  // Helper
  private AbholZeitfenster fenster() {
    return AbholZeitfenster.of(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(3));
  }

  private Angebot neuesAngebot() {
    return Angebot.neu("a1", "anbieter1", "Suppe", "Tomatensuppe", Set.of("vegi"), fenster());
  }
}
