package com.foodrescue.userverwaltung.valueobjects;

import java.util.Objects;

public final class Name {

  private final String value;

  public Name(String value) {
    Objects.requireNonNull(value, "Name darf nicht null sein");
    String trimmed = value.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("Name darf nicht leer sein");
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

  // equals / hashCode für Value Object Gleichheit
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Name)) return false;
    Name name = (Name) o;
    return value.equals(name.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
