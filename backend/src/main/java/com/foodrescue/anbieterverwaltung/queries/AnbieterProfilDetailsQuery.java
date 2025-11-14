package com.foodrescue.anbieterverwaltung.queries;

import java.util.Objects;
import java.util.UUID;

public class AnbieterProfilDetailsQuery {

  private final UUID profilId;

  public AnbieterProfilDetailsQuery(UUID profilId) {
    this.profilId = Objects.requireNonNull(profilId, "ProfilId darf nicht null sein");
  }

  public UUID getProfilId() {
    return profilId;
  }
}
