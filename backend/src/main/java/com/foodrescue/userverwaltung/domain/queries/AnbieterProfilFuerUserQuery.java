package com.foodrescue.userverwaltung.domain.queries;

import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.model.User;

public class AnbieterProfilFuerUserQuery {

  private final User user;
  private final AnbieterProfil anbieterProfil;

  public AnbieterProfilFuerUserQuery(User user, AnbieterProfil anbieterProfil) {
    this.user = user;
    this.anbieterProfil = anbieterProfil;
  }

  public User getUser() {
    return user;
  }

  public AnbieterProfil getAnbieterProfil() {
    return anbieterProfil;
  }
}
