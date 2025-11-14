package com.foodrescue.angebotsmanagement.valueobjects;

import com.foodrescue.shared.exception.DomainException;

public final class AngebotsId {
  private final String value;

  private AngebotsId(String value) {
    if (value == null || value.isBlank()) throw DomainException.raiseIdInvalid("AngebotsId");
    this.value = value;
  }

  public static AngebotsId of(String value) {
    return new AngebotsId(value);
  }

  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof AngebotsId id && value.equals(id.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
