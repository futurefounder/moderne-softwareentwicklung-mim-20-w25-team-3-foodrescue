package com.foodrescue.angebotsmanagement.domain.model;

/**
 * Aggregate Root: Angebot
 *
 * <p>Repräsentiert ein Lebensmittelangebot, das von einem Anbieter zur Rettung bereitgestellt wird.
 *
 * <p>Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.events.AngebotErstelltEvent;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.*;

public class Angebot {

  public enum Status {
    ENTWURF,
    VERFUEGBAR,
    RESERVIERT,
    ABGEHOLT,
    ENTFERNT
  }

  private final String id;
  private final String anbieterId;
  private String titel;
  private String beschreibung;
  private Set<String> tags;
  private AbholZeitfenster zeitfenster;
  private Status status = Status.ENTWURF; // noch nicht veröffentlicht
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private Angebot(
      String id,
      String anbieterId,
      String titel,
      String beschreibung,
      Set<String> tags,
      AbholZeitfenster zeitfenster) {
    if (id == null || anbieterId == null || titel == null || zeitfenster == null) {
      throw new DomainException("Angebot unvollständig");
    }
    this.id = id;
    this.anbieterId = anbieterId;
    this.titel = titel;
    this.beschreibung = beschreibung == null ? "" : beschreibung;
    this.tags = tags == null ? Set.of() : Set.copyOf(tags);
    this.zeitfenster = zeitfenster;
  }

  // Neue Fabrikmethode für Erstellung mit Event
  public static Angebot erstelle(
      AngebotsId id,
      UserId anbieterId,
      String titel,
      String beschreibung,
      Set<String> tags,
      AbholZeitfenster zeitfenster) {
    var angebot =
        new Angebot(
            id.value(), anbieterId.getValue().toString(), titel, beschreibung, tags, zeitfenster);
    angebot.domainEvents.add(new AngebotErstelltEvent(id.value()));
    return angebot;
  }

  // Methode zum Aktualisieren (z. B. für Anbieter)
  public void aktualisiere(
      String neuerTitel,
      String neueBeschreibung,
      Set<String> neueTags,
      AbholZeitfenster neuesFenster) {
    if (status != Status.ENTWURF && status != Status.VERFUEGBAR) {
      throw new DomainException("Angebot kann nur im Entwurf oder verfügbar aktualisiert werden");
    }
    this.titel = neuerTitel;
    this.beschreibung = neueBeschreibung;
    this.tags = neueTags;
    this.zeitfenster = neuesFenster;
  }

  public List<DomainEvent> veroeffentlichen() {
    if (status != Status.ENTWURF) {
      throw new DomainException("Angebot ist bereits veröffentlicht oder aktiv");
    }
    status = Status.VERFUEGBAR;
    var evt = new AngebotErstelltEvent(id);
    domainEvents.add(evt);
    return List.of(evt);
  }

  public Reservierung reservieren(String abholerId, Abholcode code) {
    if (status != Status.VERFUEGBAR) {
      throw new DomainException("Angebot ist nicht verfügbar");
    }
    status = Status.RESERVIERT;
    return Reservierung.erstelle(UUID.randomUUID().toString(), id, abholerId, code);
  }

  // Getter
  public String getId() {
    return id;
  }

  public Status getStatus() {
    return status;
  }

  public AbholZeitfenster getZeitfenster() {
    return zeitfenster;
  }

  public List<DomainEvent> getDomainEvents() {
    return List.copyOf(domainEvents);
  }

  public String getTitel() {
    return titel;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public Set<String> getTags() {
    return Set.copyOf(tags);
  }

  public String getAnbieterId() {
    return anbieterId;
  }
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
  // TELEFON_PATTERN: ^(\+49|0)[1-9](?=(?:[^\d]*\d){4})[0-9\s\-/]+$
  // - Startet mit +49 oder 0
  // - Gefolgt von Ziffer 1-9
  // - Stellt sicher (per Lookahead), dass mindestens 4 weitere Ziffern im Rest der Nummer vorkommen
  // - Erlaubt, dass diese Ziffern von Leerzeichen, - oder / durchsetzt sein können
  //
// }
