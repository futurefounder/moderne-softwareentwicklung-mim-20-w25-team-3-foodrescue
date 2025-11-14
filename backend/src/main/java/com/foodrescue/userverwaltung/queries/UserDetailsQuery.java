package com.foodrescue.userverwaltung.queries;

import java.util.Objects;
import java.util.UUID;

public class UserDetailsQuery {

  private final UUID userId;

  public UserDetailsQuery(UUID userId) {
    this.userId = Objects.requireNonNull(userId, "UserId darf nicht null sein");
  }

  public UUID getUserId() {
    return userId;
  }
}
