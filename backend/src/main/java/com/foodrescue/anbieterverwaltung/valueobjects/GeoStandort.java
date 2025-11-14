package com.foodrescue.anbieterverwaltung.valueobjects;

import java.util.Objects;

public final class GeoStandort {

  private final double breitengrad;
  private final double laengengrad;

  public GeoStandort(double breitengrad, double laengengrad) {
    if (breitengrad < -90.0 || breitengrad > 90.0) {
      throw new IllegalArgumentException("Breitengrad muss zwischen -90 und 90 liegen");
    }
    if (laengengrad < -180.0 || laengengrad > 180.0) {
      throw new IllegalArgumentException("Laengengrad muss zwischen -180 und 180 liegen");
    }
    this.breitengrad = breitengrad;
    this.laengengrad = laengengrad;
  }

  public double getBreitengrad() {
    return breitengrad;
  }

  public double getLaengengrad() {
    return laengengrad;
  }

  @Override
  public String toString() {
    return "GeoStandort{" + "breitengrad=" + breitengrad + ", laengengrad=" + laengengrad + '}';
  }

  // equals / hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GeoStandort)) return false;
    GeoStandort that = (GeoStandort) o;
    return Double.compare(that.breitengrad, breitengrad) == 0
        && Double.compare(that.laengengrad, laengengrad) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(breitengrad, laengengrad);
  }
}
