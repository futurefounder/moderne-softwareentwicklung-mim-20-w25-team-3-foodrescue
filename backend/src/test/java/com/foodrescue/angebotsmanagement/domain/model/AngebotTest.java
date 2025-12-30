package com.foodrescue.angebotsmanagement.domain.model;

import static org.assertj.core.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.events.AngebotErstelltEvent;
import com.foodrescue.angebotsmanagement.domain.events.AngebotReserviertEvent;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Angebot Aggregate Root")
class AngebotTest {

  private AngebotsId angebotId;
  private UserId anbieterId;
  private UserId abholerId;
  private String titel;
  private String beschreibung;
  private Set<String> tags;
  private AbholZeitfenster zeitfenster;

  @BeforeEach
  void setUp() {
    angebotId = AngebotsId.of(UUID.randomUUID().toString());
    anbieterId = new UserId(UUID.randomUUID());
    abholerId = new UserId(UUID.randomUUID());
    titel = "Frisches Gemüse";
    beschreibung = "Bio-Gemüse vom Markt";
    tags = Set.of("bio", "gemüse");

    LocalDateTime von = LocalDateTime.now().minusHours(2);
    LocalDateTime bis = von.plusHours(4);
    zeitfenster = new AbholZeitfenster(von, bis);
  }

  @Nested
  @DisplayName("Erstellung eines Angebots")
  class ErstellungTests {

    @Test
    @DisplayName("Erfolgreich ein neues Angebot erstellen")
    void erstelleAngebot_Erfolgreich() {
      // Act
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);

      // Assert
      assertThat(angebot).isNotNull();
      assertThat(angebot.getId()).isEqualTo(angebotId.value());
      assertThat(angebot.getTitel()).isEqualTo(titel);
      assertThat(angebot.getBeschreibung()).isEqualTo(beschreibung);
      assertThat(angebot.getTags()).containsExactlyInAnyOrderElementsOf(tags);
      assertThat(angebot.getStatus()).isEqualTo(Angebot.Status.ENTWURF);
    }

    @Test
    @DisplayName("AngebotErstelltEvent wird erzeugt")
    void erstelleAngebot_ErstelltEvent() {
      // Act
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);

      // Assert
      List<DomainEvent> events = angebot.getDomainEvents();
      assertThat(events).hasSize(1);
      assertThat(events.get(0)).isInstanceOf(AngebotErstelltEvent.class);
    }

    @Test
    @DisplayName("Fehler wenn Titel leer ist")
    void erstelleAngebot_FehlerBeiLeeremTitel() {
      // Act & Assert
      assertThatThrownBy(
              () -> Angebot.erstelle(angebotId, anbieterId, "", beschreibung, tags, zeitfenster))
          .isInstanceOf(DomainException.class)
          .hasMessageContaining("Titel darf nicht leer sein");
    }

    @Test
    @DisplayName("Fehler wenn Titel zu lang ist")
    void erstelleAngebot_FehlerBeiZuLangemTitel() {
      // Arrange
      String zuLangerTitel = "a".repeat(101);

      // Act & Assert
      assertThatThrownBy(
              () ->
                  Angebot.erstelle(
                      angebotId, anbieterId, zuLangerTitel, beschreibung, tags, zeitfenster))
          .isInstanceOf(DomainException.class)
          .hasMessageContaining("maximal 100 Zeichen");
    }

    @Test
    @DisplayName("Fehler wenn AngebotsId null ist")
    void erstelleAngebot_FehlerBeiNullId() {
      // Act & Assert
      assertThatThrownBy(
              () -> Angebot.erstelle(null, anbieterId, titel, beschreibung, tags, zeitfenster))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Fehler wenn Zeitfenster null ist")
    void erstelleAngebot_FehlerBeiNullZeitfenster() {
      // Act & Assert
      assertThatThrownBy(
              () -> Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, null))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("Veröffentlichung eines Angebots")
  class VeroeffentlichungTests {

    @Test
    @DisplayName("Erfolgreich ein Angebot veröffentlichen")
    void veroeffentlichen_Erfolgreich() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);

      // Act
      List<DomainEvent> events = angebot.veroeffentlichen();

      // Assert
      assertThat(angebot.getStatus()).isEqualTo(Angebot.Status.VERFUEGBAR);
      assertThat(events).isNotEmpty();
    }

    @Test
    @DisplayName("Fehler wenn bereits veröffentlicht")
    void veroeffentlichen_FehlerWennBereitsVeroeffentlicht() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);
      angebot.veroeffentlichen();

      // Act & Assert
      assertThatThrownBy(() -> angebot.veroeffentlichen())
          .isInstanceOf(DomainException.class)
          .hasMessageContaining("bereits veröffentlicht");
    }
  }

  @Nested
  @DisplayName("Reservierung eines Angebots")
  class ReservierungTests {

    private Angebot verfuegbaresAngebot;
    private Abholcode abholcode;

    @BeforeEach
    void setUp() {
      verfuegbaresAngebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);
      verfuegbaresAngebot.veroeffentlichen();
      verfuegbaresAngebot.clearDomainEvents(); // Clear für saubere Tests

      abholcode = Abholcode.of("ABC123");
    }

    @Test
    @DisplayName("Erfolgreich ein Angebot reservieren")
    void reservieren_Erfolgreich() {
      // Act
      AngebotReserviertEvent event =
          verfuegbaresAngebot.reservieren(abholerId.getValue().toString(), abholcode);

      // Assert
      assertThat(verfuegbaresAngebot.getStatus()).isEqualTo(Angebot.Status.RESERVIERT);
      assertThat(event).isNotNull();
      assertThat(event.getAngebotId()).isEqualTo(angebotId.value());
      assertThat(event.getAbholerId()).isEqualTo(abholerId.getValue().toString());
    }

    @Test
    @DisplayName("AngebotReserviertEvent wird erzeugt")
    void reservieren_EventWirdErzeugt() {
      // Act
      verfuegbaresAngebot.reservieren(abholerId.getValue().toString(), abholcode);

      // Assert
      List<DomainEvent> events = verfuegbaresAngebot.getDomainEvents();
      assertThat(events).hasSize(1);
      assertThat(events.get(0)).isInstanceOf(AngebotReserviertEvent.class);
    }

    @Test
    @DisplayName("Fehler wenn Angebot nicht verfügbar ist")
    void reservieren_FehlerWennNichtVerfuegbar() {
      // Arrange
      Angebot entwurfAngebot =
          Angebot.erstelle(
              AngebotsId.of(UUID.randomUUID().toString()),
              anbieterId,
              titel,
              beschreibung,
              tags,
              zeitfenster);

      // Act & Assert
      assertThatThrownBy(
              () -> entwurfAngebot.reservieren(abholerId.getValue().toString(), abholcode))
          .isInstanceOf(DomainException.class)
          .hasMessageContaining("nicht verfügbar");
    }

    @Test
    @DisplayName("Fehler wenn Anbieter sein eigenes Angebot reservieren will")
    void reservieren_FehlerWennAnbieterReserviert() {
      // Act & Assert
      assertThatThrownBy(
              () -> verfuegbaresAngebot.reservieren(anbieterId.getValue().toString(), abholcode))
          .isInstanceOf(DomainException.class)
          .hasMessageContaining("eigenes Angebot");
    }
  }

  @Nested
  @DisplayName("Aktualisierung eines Angebots")
  class AktualisierungTests {

    @Test
    @DisplayName("Erfolgreich ein Angebot im Entwurf aktualisieren")
    void aktualisiere_ErfolgreichImEntwurf() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);
      String neuerTitel = "Aktualisierter Titel";

      // Act
      angebot.aktualisiere(neuerTitel, beschreibung, tags, zeitfenster);

      // Assert
      assertThat(angebot.getTitel()).isEqualTo(neuerTitel);
    }

    @Test
    @DisplayName("Fehler wenn Angebot reserviert ist")
    void aktualisiere_FehlerWennReserviert() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);
      angebot.veroeffentlichen();
      angebot.reservieren(abholerId.getValue().toString(), Abholcode.of("ABC123"));

      // Act & Assert
      assertThatThrownBy(() -> angebot.aktualisiere("Neuer Titel", beschreibung, tags, zeitfenster))
          .isInstanceOf(DomainException.class)
          .hasMessageContaining("kann nur im Entwurf oder verfügbar aktualisiert werden");
    }
  }

  @Nested
  @DisplayName("Domain Logic Tests")
  class DomainLogicTests {

    @Test
    @DisplayName("istVerfuegbar() gibt true für verfügbare Angebote")
    void istVerfuegbar_TrueFuerVerfuegbareAngebote() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);
      angebot.veroeffentlichen();

      // Act & Assert
      assertThat(angebot.istVerfuegbar()).isTrue();
    }

    @Test
    @DisplayName("istVerfuegbar() gibt false für Entwürfe")
    void istVerfuegbar_FalseFuerEntwurf() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);

      // Act & Assert
      assertThat(angebot.istVerfuegbar()).isFalse();
    }

    @Test
    @DisplayName("kannReserviertWerdenVon() prüft korrekt")
    void kannReserviertWerdenVon_PrueftKorrekt() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);
      angebot.veroeffentlichen();

      // Act & Assert
      assertThat(angebot.kannReserviertWerdenVon(abholerId)).isTrue();
      assertThat(angebot.kannReserviertWerdenVon(anbieterId)).isFalse();
    }

    @Test
    @DisplayName("passztZuSuchbegriff() findet im Titel")
    void passztZuSuchbegriff_FindetImTitel() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(
              angebotId, anbieterId, "Frisches Gemüse", beschreibung, tags, zeitfenster);

      // Act & Assert
      assertThat(angebot.passztZuSuchbegriff("gemüse")).isTrue();
      assertThat(angebot.passztZuSuchbegriff("GEMÜSE")).isTrue();
      assertThat(angebot.passztZuSuchbegriff("frisch")).isTrue();
      assertThat(angebot.passztZuSuchbegriff("pizza")).isFalse();
    }
  }

  @Nested
  @DisplayName("AggregateRoot Interface Tests")
  class AggregateRootTests {

    @Test
    @DisplayName("clearDomainEvents() löscht alle Events")
    void clearDomainEvents_LoeschtAlleEvents() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);
      assertThat(angebot.getDomainEvents()).isNotEmpty();

      // Act
      angebot.clearDomainEvents();

      // Assert
      assertThat(angebot.getDomainEvents()).isEmpty();
    }

    @Test
    @DisplayName("getDomainEvents() gibt unveränderliche Kopie zurück")
    void getDomainEvents_GibtUnveraenderlicheKopieZurueck() {
      // Arrange
      Angebot angebot =
          Angebot.erstelle(angebotId, anbieterId, titel, beschreibung, tags, zeitfenster);

      // Act
      List<DomainEvent> events = angebot.getDomainEvents();

      // Assert
      assertThatThrownBy(() -> events.clear()).isInstanceOf(UnsupportedOperationException.class);
    }
  }
}
