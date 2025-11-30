package com.foodrescue.userverwaltung.infrastructure.web.rest;

public class ErstelleAnbieterProfilRequest {

  private String userId;
  private String geschaeftsname;
  private String geschaeftstyp;
  private String adresse;
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
