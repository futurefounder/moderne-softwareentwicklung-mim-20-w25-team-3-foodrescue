package com.foodrescue.userverwaltung.application.commands;

import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;

public class RegistriereUserCommand {

  private final Name name;
  private final EmailAdresse email;
  private final Rolle rolle;

  public RegistriereUserCommand(Name name, EmailAdresse email, Rolle rolle) {
    this.name = name;
    this.email = email;
    this.rolle = rolle;
  }

  public Name getName() {
    return name;
  }

  public EmailAdresse getEmail() {
    return email;
  }

  public Rolle getRolle() {
    return rolle;
  }
}
