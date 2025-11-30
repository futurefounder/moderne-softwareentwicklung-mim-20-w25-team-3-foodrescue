package com.foodrescue.userverwaltung.domain.valueobjects;

import com.foodrescue.shared.exception.DomainException;
import java.util.Objects;
import java.util.UUID;

public final class UserId {

  private final UUID value;

  public UserId(UUID value) {
    if (value == null) {
      throw DomainException.raiseIdInvalid("UserId");
    }
    this.value = value;
  }

  public static UserId neu() {
    return new UserId(UUID.randomUUID());
  }

  public UUID getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserId)) return false;
    UserId userId = (UserId) o;
    return value.equals(userId.value);
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
