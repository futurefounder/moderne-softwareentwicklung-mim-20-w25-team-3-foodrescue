package com.foodrescue.abholungsmanagement.model;

import com.foodrescue.abholungsmanagement.events.AbholungAbgeschlossen;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import java.time.Instant;
import java.util.List;

/**
 * Aggregate Root: Abholung
 *
 * <p>Repräsentiert den Abholvorgang einer reservierten Lebensmittelrettung.
 *
 * <p>Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
public class Abholung {

  public enum Status {
    ANGELEGT,
    ABGESCHLOSSEN,
    FEHLGESCHLAGEN
  }

  private final String id;
  private final String reservierungsId;
  private final Abholcode abholcode;
  private final Instant angelegtAm;
  private Status status;
  private Instant abgeschlossenAm;

  public Abholung(String id, String reservierungsId, Abholcode abholcode) {
    if (id == null || reservierungsId == null || abholcode == null)
      throw new DomainException("Abholung unvollständig");
    this.id = id;
    this.reservierungsId = reservierungsId;
    this.abholcode = abholcode;
    this.status = Status.ANGELEGT;
    this.angelegtAm = Instant.now();
  }

  /** Bestätigt die Abholung, falls der Code korrekt ist. Gibt das ausgelöste DomainEvent zurück. */
  public List<DomainEvent> bestaetigen(Abholcode eingegeben) {
    if (status != Status.ANGELEGT) {
      throw new DomainException("Abholung wurde bereits verarbeitet");
    }

    if (!abholcode.equals(eingegeben)) {
      status = Status.FEHLGESCHLAGEN;
      throw new DomainException("Falscher Abholcode");
    }

    status = Status.ABGESCHLOSSEN;
    abgeschlossenAm = Instant.now();
    return List.of(new AbholungAbgeschlossen(reservierungsId));
  }

  // Getter
  public String getId() {
    return id;
  }

  public String getReservierungsId() {
    return reservierungsId;
  }

  public Abholcode getAbholcode() {
    return abholcode;
  }

  public Status getStatus() {
    return status;
  }

  public Instant getAngelegtAm() {
    return angelegtAm;
  }

  public Instant getAbgeschlossenAm() {
    return abgeschlossenAm;
  }
}
