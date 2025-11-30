package com.foodrescue.userverwaltung.domain.valueobjects;

import java.util.Objects;

public final class Adresse {

  private final String strasse;
  private final String plz;
  private final String ort;
  private final String land;

  public Adresse(String strasse, String plz, String ort, String land) {
    this.strasse = validate("Strasse", strasse);
    this.plz = validate("PLZ", plz);
    this.ort = validate("Ort", ort);
    this.land = validate("Land", land);
  }

  private String validate(String feldName, String wert) {
    Objects.requireNonNull(wert, feldName + " darf nicht null sein");
    String trimmed = wert.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException(feldName + " darf nicht leer sein");
    }
    return trimmed;
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

  @Override
  public String toString() {
    return strasse + ", " + plz + " " + ort + ", " + land;
  }

  // equals / hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Adresse)) return false;
    Adresse adresse = (Adresse) o;
    return strasse.equals(adresse.strasse)
        && plz.equals(adresse.plz)
        && ort.equals(adresse.ort)
        && land.equals(adresse.land);
  }

  @Override
  public int hashCode() {
    return Objects.hash(strasse, plz, ort, land);
  }
}
