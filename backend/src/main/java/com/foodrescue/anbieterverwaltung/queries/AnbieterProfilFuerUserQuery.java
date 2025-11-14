package com.foodrescue.anbieterverwaltung.queries;

import java.util.Objects;
import java.util.UUID;

public class AnbieterProfilFuerUserQuery {

  private final UUID userId;

  public AnbieterProfilFuerUserQuery(UUID userId) {
    this.userId = Objects.requireNonNull(userId, "UserId darf nicht null sein");
  }

  public UUID getUserId() {
    return userId;
  }
}
