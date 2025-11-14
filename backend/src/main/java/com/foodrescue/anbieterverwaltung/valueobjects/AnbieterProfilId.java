package com.foodrescue.anbieterverwaltung.valueobjects;

import java.util.Objects;
import java.util.UUID;

public final class AnbieterProfilId {

  private final UUID value;

  public AnbieterProfilId(UUID value) {
    this.value = Objects.requireNonNull(value, "AnbieterProfilId darf nicht null sein");
  }

  public static AnbieterProfilId neu() {
    return new AnbieterProfilId(UUID.randomUUID());
  }

  public UUID getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AnbieterProfilId)) return false;
    AnbieterProfilId that = (AnbieterProfilId) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
