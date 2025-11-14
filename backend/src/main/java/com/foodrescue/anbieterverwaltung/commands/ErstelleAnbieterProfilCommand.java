package com.foodrescue.anbieterverwaltung.commands;

import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.valueobjects.Rolle;
import java.util.Objects;
import java.util.UUID;

public class ErstelleAnbieterProfilCommand {

  private final UUID userId;
  private final Rolle userRolle;
  private final String geschaeftsname;
  private final Geschaeftstyp geschaeftstyp;

  private final String strasse;
  private final String plz;
  private final String ort;
  private final String land;

  private final Double breitengrad; // optional
  private final Double laengengrad; // optional

  public ErstelleAnbieterProfilCommand(
      UUID userId,
      Rolle userRolle,
      String geschaeftsname,
      Geschaeftstyp geschaeftstyp,
      String strasse,
      String plz,
      String ort,
      String land,
      Double breitengrad,
      Double laengengrad) {

    this.userId = Objects.requireNonNull(userId, "UserId darf nicht null sein");
    this.userRolle = Objects.requireNonNull(userRolle, "UserRolle darf nicht null sein");
    this.geschaeftsname =
        Objects.requireNonNull(geschaeftsname, "Geschaeftsname darf nicht null sein");
    this.geschaeftstyp =
        Objects.requireNonNull(geschaeftstyp, "Geschaeftstyp darf nicht null sein");
    this.strasse = Objects.requireNonNull(strasse, "Strasse darf nicht null sein");
    this.plz = Objects.requireNonNull(plz, "PLZ darf nicht null sein");
    this.ort = Objects.requireNonNull(ort, "Ort darf nicht null sein");
    this.land = Objects.requireNonNull(land, "Land darf nicht null sein");
    this.breitengrad = breitengrad;
    this.laengengrad = laengengrad;
  }

  public UUID getUserId() {
    return userId;
  }

  public Rolle getUserRolle() {
    return userRolle;
  }

  public String getGeschaeftsname() {
    return geschaeftsname;
  }

  public Geschaeftstyp getGeschaeftstyp() {
    return geschaeftstyp;
  }

  public String getStrasse() {
    return strasse;
  }

  public String getPlz() {
    return plz;
  }

  public String getOrt() {
    return ort;
  }

  public String getLand() {
    return land;
  }

  public Double getBreitengrad() {
    return breitengrad;
  }

  public Double getLaengengrad() {
    return laengengrad;
  }

  public boolean hatGeoStandort() {
    return breitengrad != null && laengengrad != null;
  }
}
