package com.foodrescue.angebotsmanagement.model;

/**
 * Aggregate Root: Angebot
 *
 * <p>Repräsentiert ein Lebensmittelangebot, das von einem Anbieter zur Rettung bereitgestellt wird.
 *
 * <p>Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
import com.foodrescue.abholungsmanagement.model.AbholZeitfenster;
import com.foodrescue.abholungsmanagement.model.Abholcode;
import com.foodrescue.angebotsmanagement.events.AngebotVeroeffentlicht;
import com.foodrescue.reservierungsmanagement.model.Reservierung;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import java.util.*;

public class Angebot {

  public enum Status {
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
  private Status status = Status.ENTFERNT; // noch nicht veröffentlicht
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

  public static Angebot neu(
      String id,
      String anbieterId,
      String titel,
      String beschreibung,
      Set<String> tags,
      AbholZeitfenster fenster) {
    return new Angebot(id, anbieterId, titel, beschreibung, tags, fenster);
  }

  public List<DomainEvent> veroeffentlichen() {
    if (status != Status.ENTFERNT) {
      throw new DomainException("Angebot ist bereits veröffentlicht oder aktiv");
    }
    status = Status.VERFUEGBAR;
    var evt = new AngebotVeroeffentlicht(id);
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
