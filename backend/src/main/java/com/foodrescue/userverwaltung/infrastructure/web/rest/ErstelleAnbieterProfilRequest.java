package com.foodrescue.userverwaltung.infrastructure.web.rest;

public class ErstelleAnbieterProfilRequest {

  private String userId;
  private String geschaeftsname;
  private String geschaeftstyp;

  /**
   * Legacy/Ein-Feld-Adresse (z.B. "Ahornstraße 11, 12345 Berlin, DE"). Wenn die strukturierten
   * Felder (strasse/plz/ort/land) befüllt sind, werden diese bevorzugt.
   */
  private String adresse;

  // Empfohlen: strukturierte Adresse (passt zu Domain-VO Adresse)
  private String strasse;
  private String plz;
  private String ort;
  private String land;

  private Double breitengrad; // optional
  private Double laengengrad; // optional

  public ErstelleAnbieterProfilRequest() {}

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getGeschaeftsname() {
    return geschaeftsname;
  }

  public void setGeschaeftsname(String geschaeftsname) {
    this.geschaeftsname = geschaeftsname;
  }

  public String getGeschaeftstyp() {
    return geschaeftstyp;
  }

  public void setGeschaeftstyp(String geschaeftstyp) {
    this.geschaeftstyp = geschaeftstyp;
  }

  public String getAdresse() {
    return adresse;
  }

  public void setAdresse(String adresse) {
    this.adresse = adresse;
  }

  public String getStrasse() {
    return strasse;
  }

  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  public String getPlz() {
    return plz;
  }

  public void setPlz(String plz) {
    this.plz = plz;
  }

  public String getOrt() {
    return ort;
  }

  public void setOrt(String ort) {
    this.ort = ort;
  }

  public String getLand() {
    return land;
  }

  public void setLand(String land) {
    this.land = land;
  }

  public Double getBreitengrad() {
    return breitengrad;
  }

  public void setBreitengrad(Double breitengrad) {
    this.breitengrad = breitengrad;
  }

  public Double getLaengengrad() {
    return laengengrad;
  }

  public void setLaengengrad(Double laengengrad) {
    this.laengengrad = laengengrad;
  }
}
