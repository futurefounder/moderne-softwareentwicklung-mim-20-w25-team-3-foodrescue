package com.foodrescue.userverwaltung.commands;

import com.foodrescue.userverwaltung.valueobjects.Rolle;
import java.util.Objects;

public class RegistriereUserCommand {

  private final String name;
  private final String email;
  private final Rolle rolle;

  public RegistriereUserCommand(String name, String email, Rolle rolle) {
    this.name = Objects.requireNonNull(name, "Name darf nicht null sein");
    this.email = Objects.requireNonNull(email, "Email darf nicht null sein");
    this.rolle = Objects.requireNonNull(rolle, "Rolle darf nicht null sein");
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Rolle getRolle() {
    return rolle;
  }
}
