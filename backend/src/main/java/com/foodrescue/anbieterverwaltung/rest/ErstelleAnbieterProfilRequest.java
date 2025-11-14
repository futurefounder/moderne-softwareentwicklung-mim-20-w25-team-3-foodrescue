package com.foodrescue.anbieterverwaltung.rest;

import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.valueobjects.Rolle;
import java.util.UUID;

public class ErstelleAnbieterProfilRequest {

  private UUID userId;
  private Rolle userRolle;
  private String geschaeftsname;
  private Geschaeftstyp geschaeftstyp;

  private String strasse;
  private String plz;
  private String ort;
  private String land;

  private Double breitengrad;
  private Double laengengrad;

  public ErstelleAnbieterProfilRequest() {}

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

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setUserRolle(Rolle userRolle) {
    this.userRolle = userRolle;
  }

  public void setGeschaeftsname(String geschaeftsname) {
    this.geschaeftsname = geschaeftsname;
  }

  public void setGeschaeftstyp(Geschaeftstyp geschaeftstyp) {
    this.geschaeftstyp = geschaeftstyp;
  }

  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  public void setPlz(String plz) {
    this.plz = plz;
  }

  public void setOrt(String ort) {
    this.ort = ort;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public void setBreitengrad(Double breitengrad) {
    this.breitengrad = breitengrad;
  }

  public void setLaengengrad(Double laengengrad) {
    this.laengengrad = laengengrad;
  }
}
