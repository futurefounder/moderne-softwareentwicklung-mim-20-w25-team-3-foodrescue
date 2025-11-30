package com.foodrescue.userverwaltung.infrastructure.web.rest;

import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftstyp;
import java.util.UUID;

public class AnbieterProfilResponse {

  private final UUID id;
  private final UUID userId;
  private final String geschaeftsname;
  private final Geschaeftstyp geschaeftstyp;

  private final String strasse;
  private final String plz;
  private final String ort;
  private final String land;

  private final Double breitengrad;
  private final Double laengengrad;

  public AnbieterProfilResponse(
      UUID id,
      UUID userId,
      String geschaeftsname,
      Geschaeftstyp geschaeftstyp,
      String strasse,
      String plz,
      String ort,
      String land,
      Double breitengrad,
      Double laengengrad) {
    this.id = id;
    this.userId = userId;
    this.geschaeftsname = geschaeftsname;
    this.geschaeftstyp = geschaeftstyp;
    this.strasse = strasse;
    this.plz = plz;
    this.ort = ort;
    this.land = land;
    this.breitengrad = breitengrad;
    this.laengengrad = laengengrad;
  }

  public static AnbieterProfilResponse fromDomain(AnbieterProfil profil) {
    Double lat = profil.getGeoStandort().map(g -> g.getBreitengrad()).orElse(null);
    Double lon = profil.getGeoStandort().map(g -> g.getLaengengrad()).orElse(null);

    return new AnbieterProfilResponse(
        profil.getId().getValue(),
        profil.getUserId().getValue(),
        profil.getGeschaeftsname().getValue(),
        profil.getGeschaeftstyp(),
        profil.getAdresse().getStrasse(),
        profil.getAdresse().getPlz(),
        profil.getAdresse().getOrt(),
        profil.getAdresse().getLand(),
        lat,
        lon);
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
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
}
