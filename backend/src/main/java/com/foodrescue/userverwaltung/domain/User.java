package com.foodrescue.userverwaltung.domain;

import com.foodrescue.userverwaltung.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.valueobjects.Name;
import com.foodrescue.userverwaltung.valueobjects.Rolle;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.Objects;

public class User {

  private final UserId id;
  private Name name;
  private EmailAdresse email;
  private Rolle rolle;

  public User(UserId id, Name name, EmailAdresse email, Rolle rolle) {
    this.id = Objects.requireNonNull(id, "UserId darf nicht null sein");
    this.name = Objects.requireNonNull(name, "Name darf nicht null sein");
    this.email = Objects.requireNonNull(email, "Email darf nicht null sein");
    this.rolle = Objects.requireNonNull(rolle, "Rolle darf nicht null sein");
  }

  public static User registrieren(UserId id, Name name, EmailAdresse email, Rolle rolle) {
    return new User(id, name, email, rolle);
  }

  public UserId getId() {
    return id;
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

  public void aendereName(Name neuerName) {
    this.name = Objects.requireNonNull(neuerName, "Neuer Name darf nicht null sein");
  }

  public void aendereEmail(EmailAdresse neueEmail) {
    this.email = Objects.requireNonNull(neueEmail, "Neue Email darf nicht null sein");
  }

  public void aendereRolle(Rolle neueRolle) {
    this.rolle = Objects.requireNonNull(neueRolle, "Neue Rolle darf nicht null sein");
  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name=" + name + ", email=" + email + ", rolle=" + rolle + '}';
  }
}
