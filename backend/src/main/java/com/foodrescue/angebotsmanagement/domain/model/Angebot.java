package com.foodrescue.angebotsmanagement.domain.model;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.events.AngebotErstelltEvent;
import com.foodrescue.angebotsmanagement.domain.events.AngebotReserviertEvent;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.shared.domain.AggregateRoot;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/** Aggregate Root: Angebot */
public class Angebot implements AggregateRoot {

  public enum Status {
    ENTWURF,
    VERFUEGBAR,
    RESERVIERT,
    ABGEHOLT,
    ENTFERNT
  }

  private final AngebotsId id;
  private final UserId anbieterId;
  private String titel;
  private String beschreibung;
  private Set<String> tags;
  private AbholZeitfenster zeitfenster;
  private Status status = Status.ENTWURF;
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private Angebot(
      AngebotsId id,
      UserId anbieterId,
      String titel,
      String beschreibung,
      Set<String> tags,
      AbholZeitfenster zeitfenster) {

    // Invarianten-Schutz
    this.id = Objects.requireNonNull(id, "AngebotsId darf nicht null sein");
    this.anbieterId = Objects.requireNonNull(anbieterId, "AnbieterId darf nicht null sein");
    this.zeitfenster = Objects.requireNonNull(zeitfenster, "Zeitfenster darf nicht null sein");

    if (titel == null || titel.trim().isEmpty()) {
      throw new DomainException("Titel darf nicht leer sein");
    }
    if (titel.length() > 100) {
      throw new DomainException("Titel darf maximal 100 Zeichen lang sein");
    }

    this.titel = titel.trim();
    this.beschreibung = beschreibung == null ? "" : beschreibung.trim();
    this.tags = tags == null ? Set.of() : Set.copyOf(tags);
  }

  /**
   * Factory-Methode zum Erstellen eines neuen Angebots. Erzeugt das Angebot im Status ENTWURF und
   * löst ein AngebotErstelltEvent aus.
   */
  public static Angebot erstelle(
      AngebotsId id,
      UserId anbieterId,
      String titel,
      String beschreibung,
      Set<String> tags,
      AbholZeitfenster zeitfenster) {

    var angebot = new Angebot(id, anbieterId, titel, beschreibung, tags, zeitfenster);
    angebot.domainEvents.add(new AngebotErstelltEvent(id.value()));
    return angebot;
  }

  /** Aktualisiert die Angebotsdetails. Nur möglich im Status ENTWURF oder VERFUEGBAR. */
  public void aktualisiere(
      String neuerTitel,
      String neueBeschreibung,
      Set<String> neueTags,
      AbholZeitfenster neuesFenster) {

    // Invariante: Nur in bestimmten Status änderbar
    if (status != Status.ENTWURF && status != Status.VERFUEGBAR) {
      throw new DomainException("Angebot kann nur im Entwurf oder verfügbar aktualisiert werden");
    }

    // Invariante: Titel darf nicht leer sein
    if (neuerTitel == null || neuerTitel.trim().isEmpty()) {
      throw new DomainException("Titel darf nicht leer sein");
    }
    if (neuerTitel.length() > 100) {
      throw new DomainException("Titel darf maximal 100 Zeichen lang sein");
    }

    Objects.requireNonNull(neuesFenster, "Zeitfenster darf nicht null sein");

    this.titel = neuerTitel.trim();
    this.beschreibung = neueBeschreibung == null ? "" : neueBeschreibung.trim();
    this.tags = neueTags == null ? Set.of() : Set.copyOf(neueTags);
    this.zeitfenster = neuesFenster;
  }

  /**
   * Veröffentlicht das Angebot. Ändert den Status von ENTWURF zu VERFUEGBAR.
   *
   * @return Liste der erzeugten Domain Events
   * @throws DomainException wenn das Angebot nicht im Status ENTWURF ist
   */
  public List<DomainEvent> veroeffentlichen() {
    if (status != Status.ENTWURF) {
      throw new DomainException("Angebot ist bereits veröffentlicht oder aktiv");
    }
    status = Status.VERFUEGBAR;

    // Event wird NICHT erneut hinzugefügt, da es bereits bei erstelle() hinzugefügt wurde
    // Alternativ: Eigenes "AngebotVeroeffentlichtEvent" wenn gewünscht
    return List.copyOf(domainEvents);
  }

  /**
   * Reserviert das Angebot für einen Abholer.
   *
   * <p>WICHTIG: Diese Methode erstellt KEIN Reservierungs-Aggregate mehr! Stattdessen wird ein
   * AngebotReserviertEvent erzeugt, auf das der Reservierungsmanagement Bounded Context reagiert.
   *
   * <p>Dies respektiert die Aggregate Boundaries und vermeidet tight coupling.
   *
   * @param abholerId ID des Abholers
   * @param abholcode Der generierte Abholcode
   * @return Das erzeugte Domain Event
   * @throws DomainException wenn das Angebot nicht verfügbar ist
   */
  public AngebotReserviertEvent reservieren(String abholerId, Abholcode abholcode) {
    // Invariante: Nur verfügbare Angebote können reserviert werden
    if (status != Status.VERFUEGBAR) {
      throw new DomainException("Angebot ist nicht verfügbar");
    }

    // Business Rule: Anbieter kann nicht sein eigenes Angebot abholen
    if (this.anbieterId.getValue().toString().equals(abholerId)) {
      throw new DomainException("Anbieter kann sein eigenes Angebot nicht reservieren");
    }

    status = Status.RESERVIERT;

    var event = new AngebotReserviertEvent(id.value(), abholerId, abholcode.value());
    domainEvents.add(event);

    return event;
  }

  /**
   * Markiert das Angebot als abgeholt. Dies sollte aufgerufen werden, wenn die Abholung bestätigt
   * wurde.
   */
  public void markiereAlsAbgeholt() {
    if (status != Status.RESERVIERT) {
      throw new DomainException("Nur reservierte Angebote können als abgeholt markiert werden");
    }
    status = Status.ABGEHOLT;
  }

  /**
   * Entfernt das Angebot (Soft Delete). Ein entferntes Angebot kann nicht mehr verändert werden.
   */
  public void entfernen() {
    if (status == Status.ABGEHOLT) {
      throw new DomainException("Abgeholte Angebote können nicht entfernt werden");
    }
    status = Status.ENTFERNT;
  }

  // ========== Domain Logic (Rich Domain Model) ==========

  /** Prüft, ob das Angebot aktuell verfügbar ist. */
  public boolean istVerfuegbar() {
    return status == Status.VERFUEGBAR && zeitfenster.istNochAktuell(LocalDateTime.now());
  }

  /** Prüft, ob das Angebot von einem bestimmten User reserviert werden kann. */
  public boolean kannReserviertWerdenVon(UserId userId) {
    return istVerfuegbar() && !anbieterId.equals(userId);
  }

  /** Berechnet die verbleibende Zeit bis zum Beginn des Abholzeitfensters. */
  public Duration verbleibendeZeitBisAbholung() {
    return Duration.between(LocalDateTime.now(), zeitfenster.von());
  }

  /** Prüft, ob das Angebot im Suchbegriff vorkommt. */
  public boolean passztZuSuchbegriff(String suchbegriff) {
    if (suchbegriff == null || suchbegriff.trim().isEmpty()) {
      return true;
    }

    String search = suchbegriff.toLowerCase();
    return titel.toLowerCase().contains(search)
        || beschreibung.toLowerCase().contains(search)
        || tags.stream().anyMatch(tag -> tag.toLowerCase().contains(search));
  }

  // ========== AggregateRoot Interface ==========

  @Override
  public String getId() {
    return id.value();
  }

  @Override
  public List<DomainEvent> getDomainEvents() {
    return List.copyOf(domainEvents);
  }

  @Override
  public void clearDomainEvents() {
    domainEvents.clear();
  }

  // ========== Getter ==========

  public AngebotsId getAngebotsId() {
    return id;
  }

  public UserId getAnbieterId() {
    return anbieterId;
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

  public AbholZeitfenster getZeitfenster() {
    return zeitfenster;
  }

  public Status getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return "Angebot{"
        + "id="
        + id
        + ", anbieterId="
        + anbieterId
        + ", titel='"
        + titel
        + '\''
        + ", status="
        + status
        + ", zeitfenster="
        + zeitfenster
        + '}';
  }
}
