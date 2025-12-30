package com.foodrescue.reservierungsmanagement.domain.model;

import com.foodrescue.abholungsmanagement.domain.events.AbholungAbgeschlossen;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.events.ReservierungErstellt;
import com.foodrescue.reservierungsmanagement.domain.events.ReservierungStorniertEvent;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.shared.domain.AggregateRoot;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root: Reservierung
 *
 * <p>Reservierung eines Lebensmittelangebots durch einen Retter.
 */
public class Reservierung implements AggregateRoot {

  public enum Status {
    AKTIV,
    ABGEHOLT,
    STORNIERT
  }

  private final ReservierungsId id;
  private final String angebotId; // Reference to Angebot Aggregate
  private final String abholerId; // Reference to User Aggregate
  private final Abholcode abholcode;
  private Status status;
  private final Instant erstelltAm;
  private Instant abgeholtAm;
  private Instant storniertAm;
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private Reservierung(
      ReservierungsId id, String angebotId, String abholerId, Abholcode abholcode) {

    // Invarianten-Schutz
    this.id = Objects.requireNonNull(id, "ReservierungsId darf nicht null sein");
    this.angebotId = Objects.requireNonNull(angebotId, "AngebotId darf nicht null sein");
    this.abholerId = Objects.requireNonNull(abholerId, "AbholerId darf nicht null sein");
    this.abholcode = Objects.requireNonNull(abholcode, "Abholcode darf nicht null sein");

    this.status = Status.AKTIV;
    this.erstelltAm = Instant.now();
  }

  /**
   * Factory-Methode zum Erstellen einer neuen Reservierung.
   *
   * @param id Die eindeutige Reservierungs-ID
   * @param angebotId Die ID des reservierten Angebots
   * @param abholerId Die ID des Abholers
   * @param abholcode Der generierte Abholcode
   * @return Eine neue Reservierung im Status AKTIV
   */
  public static Reservierung erstelle(
      ReservierungsId id, String angebotId, String abholerId, Abholcode abholcode) {

    var reservierung = new Reservierung(id, angebotId, abholerId, abholcode);
    reservierung.domainEvents.add(new ReservierungErstellt(id.value()));
    return reservierung;
  }

  /**
   * Bestätigt die Abholung mit dem eingegebenen Code.
   *
   * @param eingegeben Der vom Abholer eingegebene Code
   * @return Liste der erzeugten Domain Events
   * @throws DomainException wenn der Code falsch ist oder die Reservierung nicht aktiv ist
   */
  public List<DomainEvent> bestaetigeAbholung(Abholcode eingegeben) {
    // Invariante: Nur aktive Reservierungen können abgeholt werden
    if (status != Status.AKTIV) {
      throw new DomainException("Reservierung ist nicht aktiv (Status: " + status + ")");
    }

    // Invariante: Abholcode muss korrekt sein
    if (!Objects.equals(this.abholcode, eingegeben)) {
      throw new DomainException("Abholcode ist ungültig");
    }

    status = Status.ABGEHOLT;
    abgeholtAm = Instant.now();

    var event = new AbholungAbgeschlossen(id.value());
    domainEvents.add(event);

    return List.of(event);
  }

  /**
   * Storniert die Reservierung.
   *
   * @return Liste der erzeugten Domain Events
   * @throws DomainException wenn die Reservierung nicht aktiv ist
   */
  public List<DomainEvent> stornieren() {
    // Invariante: Nur aktive Reservierungen können storniert werden
    if (status != Status.AKTIV) {
      throw new DomainException(
          "Nur aktive Reservierungen können storniert werden (Status: " + status + ")");
    }

    status = Status.STORNIERT;
    storniertAm = Instant.now();

    var event = new ReservierungStorniertEvent(id.value());
    domainEvents.add(event);

    return List.of(event);
  }

  // ========== Domain Logic (Rich Domain Model) ==========

  /** Prüft, ob die Reservierung noch aktiv ist. */
  public boolean istAktiv() {
    return status == Status.AKTIV;
  }

  /** Prüft, ob die Reservierung abgeschlossen (abgeholt) ist. */
  public boolean istAbgeschlossen() {
    return status == Status.ABGEHOLT;
  }

  /** Prüft, ob die Reservierung storniert wurde. */
  public boolean istStorniert() {
    return status == Status.STORNIERT;
  }

  /** Berechnet wie lange die Reservierung schon besteht. */
  public Duration reservierungsdauer() {
    Instant bis =
        switch (status) {
          case AKTIV -> Instant.now();
          case ABGEHOLT -> abgeholtAm;
          case STORNIERT -> storniertAm;
        };
    return Duration.between(erstelltAm, bis);
  }

  /** Prüft, ob der gegebene Code mit dem Abholcode übereinstimmt. */
  public boolean codeIstKorrekt(Abholcode eingegeben) {
    return Objects.equals(this.abholcode, eingegeben);
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

  public ReservierungsId getReservierungsId() {
    return id;
  }

  public String getAngebotId() {
    return angebotId;
  }

  public String getAbholerId() {
    return abholerId;
  }

  public Abholcode getAbholcode() {
    return abholcode;
  }

  public Status getStatus() {
    return status;
  }

  public Instant getErstelltAm() {
    return erstelltAm;
  }

  public Instant getAbgeholtAm() {
    return abgeholtAm;
  }

  public Instant getStorniertAm() {
    return storniertAm;
  }

  @Override
  public String toString() {
    return "Reservierung{"
        + "id="
        + id
        + ", angebotId='"
        + angebotId
        + '\''
        + ", abholerId='"
        + abholerId
        + '\''
        + ", status="
        + status
        + ", erstelltAm="
        + erstelltAm
        + '}';
  }
}
