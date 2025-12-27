package com.foodrescue.userverwaltung.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void registrieren_setsAllFields() {
    UserId id = new UserId(UUID.randomUUID());
    Name name = new Name("Max");
    EmailAdresse email = new EmailAdresse("max@example.com");
    User u = User.registrieren(id, name, email, Rolle.ABHOLER);

    assertEquals(id, u.getId());
    assertEquals(name, u.getName());
    assertEquals(email, u.getEmail());
    assertEquals(Rolle.ABHOLER, u.getRolle());
  }

  @Test
  void aendereName_rejectsNull() {
    User u =
        User.registrieren(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);
    assertThrows(NullPointerException.class, () -> u.aendereName(null));
  }

  @Test
  void aendereEmail_rejectsNull() {
    User u =
        User.registrieren(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);
    assertThrows(NullPointerException.class, () -> u.aendereEmail(null));
  }

  @Test
  void aendereRolle_rejectsNull() {
    User u =
        User.registrieren(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);
    assertThrows(NullPointerException.class, () -> u.aendereRolle(null));
  }

  @Test
  void aendereFields_updatesState() {
    User u =
        User.registrieren(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);

    u.aendereName(new Name("Moritz"));
    u.aendereEmail(new EmailAdresse("moritz@example.com"));
    u.aendereRolle(Rolle.ANBIETER);

    assertEquals("Moritz", u.getName().getValue());
    assertEquals("moritz@example.com", u.getEmail().getValue());
    assertEquals(Rolle.ANBIETER, u.getRolle());
  }
}
