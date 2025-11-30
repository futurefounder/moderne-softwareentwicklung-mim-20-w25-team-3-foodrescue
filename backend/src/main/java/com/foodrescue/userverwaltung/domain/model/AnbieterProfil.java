package com.foodrescue.userverwaltung.domain.model;

import com.foodrescue.userverwaltung.domain.valueobjects.Adresse;
import com.foodrescue.userverwaltung.domain.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.domain.valueobjects.GeoStandort;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftsname;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.Objects;
import java.util.Optional;

public class AnbieterProfil {

  private final AnbieterProfilId id;
  private final UserId userId;
  private Geschaeftsname geschaeftsname;
  private Geschaeftstyp geschaeftstyp;
  private Adresse adresse;
  private GeoStandort geoStandort; // optional

  private AnbieterProfil(
      AnbieterProfilId id,
      UserId userId,
      Geschaeftsname geschaeftsname,
      Geschaeftstyp geschaeftstyp,
      Adresse adresse,
      GeoStandort geoStandort) {

    this.id = Objects.requireNonNull(id, "AnbieterProfilId darf nicht null sein");
    this.userId = Objects.requireNonNull(userId, "UserId darf nicht null sein");
    this.geschaeftsname =
        Objects.requireNonNull(geschaeftsname, "Geschaeftsname darf nicht null sein");
    this.geschaeftstyp =
        Objects.requireNonNull(geschaeftstyp, "Geschaeftstyp darf nicht null sein");
    this.adresse = Objects.requireNonNull(adresse, "Adresse darf nicht null sein");
    this.geoStandort = geoStandort; // optional
  }

  /** Fabrikmethode, die sicherstellt, dass nur User mit Rolle ANBIETER ein AnbieterProfil haben. */
  public static AnbieterProfil erstellenFuerAnbieter(
      AnbieterProfilId id,
      UserId userId,
      Rolle userRolle,
      Geschaeftsname geschaeftsname,
      Geschaeftstyp geschaeftstyp,
      Adresse adresse,
      GeoStandort geoStandort) {
    Objects.requireNonNull(userRolle, "UserRolle darf nicht null sein");
    if (userRolle != Rolle.ANBIETER) {
      throw new IllegalArgumentException(
          "AnbieterProfil kann nur für User mit Rolle ANBIETER angelegt werden");
    }
    return new AnbieterProfil(id, userId, geschaeftsname, geschaeftstyp, adresse, geoStandort);
  }

  public AnbieterProfilId getId() {
    return id;
  }

  public UserId getUserId() {
    return userId;
  }

  public Geschaeftsname getGeschaeftsname() {
    return geschaeftsname;
  }

  public Geschaeftstyp getGeschaeftstyp() {
    return geschaeftstyp;
  }

  public Adresse getAdresse() {
    return adresse;
  }

  public Optional<GeoStandort> getGeoStandort() {
    return Optional.ofNullable(geoStandort);
  }

  public void aendereGeschaeftsname(Geschaeftsname neuerName) {
    this.geschaeftsname =
        Objects.requireNonNull(neuerName, "Neuer Geschaeftsname darf nicht null sein");
  }

  public void aendereGeschaeftstyp(Geschaeftstyp neuerTyp) {
    this.geschaeftstyp =
        Objects.requireNonNull(neuerTyp, "Neuer Geschaeftstyp darf nicht null sein");
  }

  public void aendereAdresse(Adresse neueAdresse) {
    this.adresse = Objects.requireNonNull(neueAdresse, "Neue Adresse darf nicht null sein");
  }

  public void setzeGeoStandort(GeoStandort neuerStandort) {
    this.geoStandort = Objects.requireNonNull(neuerStandort, "GeoStandort darf nicht null sein");
  }

  public void entferneGeoStandort() {
    this.geoStandort = null;
  }

  @Override
  public String toString() {
    return "AnbieterProfil{"
        + "id="
        + id
        + ", userId="
        + userId
        + ", geschaeftsname="
        + geschaeftsname
        + ", geschaeftstyp="
        + geschaeftstyp
        + ", adresse="
        + adresse
        + ", geoStandort="
        + geoStandort
        + '}';
  }
}
