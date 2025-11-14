package com.foodrescue.anbieterverwaltung.valueobjects;

import java.util.Objects;

public final class Geschaeftsname {

  private final String value;

  public Geschaeftsname(String value) {
    Objects.requireNonNull(value, "Geschaeftsname darf nicht null sein");
    String trimmed = value.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("Geschaeftsname darf nicht leer sein");
    }
    this.value = trimmed;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  // equals / hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Geschaeftsname)) return false;
    Geschaeftsname that = (Geschaeftsname) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
