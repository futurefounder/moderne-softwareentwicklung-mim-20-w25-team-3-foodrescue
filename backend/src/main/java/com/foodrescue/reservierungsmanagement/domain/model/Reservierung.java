package com.foodrescue.reservierungsmanagement.domain.model;

import com.foodrescue.abholungsmanagement.domain.events.AbholungAbgeschlossen;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.events.ReservierungErstellt;
import com.foodrescue.reservierungsmanagement.domain.events.ReservierungStorniertEvent;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregate Root: Reservierung
 *
 * <p>Repräsentiert eine Reservierung eines Lebensmittelangebots durch einen Retter.
 *
 * <p>Diese Klasse ist als Platzhalter für zukünftige Use-Case-driven Implementierung vorgesehen.
 */
public class Reservierung {

  public enum Status {
    AKTIV,
    ABGEHOLT,
    STORNIERT
  }

  private final String id;
  private final String angebotId;
  private final String abholerId;
  private final Abholcode abholcode;
  private Status status;
  private final Instant erstelltAm;
  private final List<DomainEvent> domainEvents = new ArrayList<>();

  private Reservierung(String id, String angebotId, String abholerId, Abholcode abholcode) {
    if (id == null || angebotId == null || abholerId == null || abholcode == null) {
      throw new DomainException("Reservierung unvollständig");
    }
    this.id = id;
    this.angebotId = angebotId;
    this.abholerId = abholerId;
    this.abholcode = abholcode;
    this.status = Status.AKTIV;
    this.erstelltAm = Instant.now();
  }

  public static Reservierung erstelle(
      String id, String angebotId, String abholerId, Abholcode code) {
    var r = new Reservierung(id, angebotId, abholerId, code);
    r.domainEvents.add(new ReservierungErstellt(id));
    return r;
  }

  public List<DomainEvent> bestaetigeAbholung(Abholcode eingegeben) {
    if (!Objects.equals(this.abholcode, eingegeben)) {
      throw new DomainException("Abholcode falsch");
    }
    if (status != Status.AKTIV) {
      throw new DomainException("Reservierung ist nicht aktiv");
    }
    status = Status.ABGEHOLT;
    var evt = new AbholungAbgeschlossen(id);
    domainEvents.add(evt);
    return List.of(evt);
  }

  public List<DomainEvent> stornieren() {
    if (status != Status.AKTIV) {
      throw new DomainException("Nur aktive Reservierungen können storniert werden");
    }
    status = Status.STORNIERT;
    domainEvents.add(new ReservierungStorniertEvent(id));
    return List.of(new ReservierungStorniertEvent(id));
  }

  // Getter

  public String getId() {
    return id;
  }

  public String getAngebotId() {
    return angebotId;
  }

  public Status getStatus() {
    return status;
  }

  public List<DomainEvent> getDomainEvents() {
    return List.copyOf(domainEvents);
  }

  public String getAbholerId() {
    return abholerId;
  }

  public Abholcode getAbholcode() {
    return abholcode;
  }
}
