package com.foodrescue.userverwaltung.application.commands;

import com.foodrescue.userverwaltung.domain.valueobjects.Adresse;
import com.foodrescue.userverwaltung.domain.valueobjects.GeoStandort;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftsname;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;

public class ErstelleAnbieterProfilCommand {

  private final UserId userId;
  private final Geschaeftsname geschaeftsname;
  private final Geschaeftstyp geschaeftstyp;
  private final Adresse adresse;

  /** darf null sein, wenn kein GeoStandort gesetzt werden soll */
  private final GeoStandort geoStandort;

  public ErstelleAnbieterProfilCommand(
      UserId userId,
      Geschaeftsname geschaeftsname,
      Geschaeftstyp geschaeftstyp,
      Adresse adresse,
      GeoStandort geoStandort) {
    this.userId = userId;
    this.geschaeftsname = geschaeftsname;
    this.geschaeftstyp = geschaeftstyp;
    this.adresse = adresse;
    this.geoStandort = geoStandort;
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

  public GeoStandort getGeoStandort() {
    return geoStandort;
  }
}
