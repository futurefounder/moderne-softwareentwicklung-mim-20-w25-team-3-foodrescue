package com.foodrescue.userverwaltung.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class UserTest {

  @Test
  void registrierenMitGueltigenWerten() {
    UserId id = new UserId(UUID.randomUUID());
    Name name = new Name("Max Mustermann");
    EmailAdresse email = new EmailAdresse("max@example.com");

    User user = User.registrieren(id, name, email, Rolle.ABHOLER);

    assertEquals(id, user.getId());
    assertEquals(name, user.getName());
    assertEquals(email, user.getEmail());
    assertEquals(Rolle.ABHOLER, user.getRolle());
  }

  @Test
  void konstruktorWirftExceptionBeiNullId() {
    Name name = new Name("Max");
    EmailAdresse email = new EmailAdresse("max@example.com");

    assertThrows(NullPointerException.class, () -> new User(null, name, email, Rolle.ANBIETER));
  }

  @Test
  void konstruktorWirftExceptionBeiNullName() {
    UserId id = new UserId(UUID.randomUUID());
    EmailAdresse email = new EmailAdresse("max@example.com");

    assertThrows(NullPointerException.class, () -> new User(id, null, email, Rolle.ANBIETER));
  }

  @Test
  void konstruktorWirftExceptionBeiNullEmail() {
    UserId id = new UserId(UUID.randomUUID());
    Name name = new Name("Max");

    assertThrows(NullPointerException.class, () -> new User(id, name, null, Rolle.ANBIETER));
  }

  @Test
  void konstruktorWirftExceptionBeiNullRolle() {
    UserId id = new UserId(UUID.randomUUID());
    Name name = new Name("Max");
    EmailAdresse email = new EmailAdresse("max@example.com");

    assertThrows(NullPointerException.class, () -> new User(id, name, email, null));
  }

  @Test
  void aendereNameUndEmailUndRolle() {
    UserId id = new UserId(UUID.randomUUID());
    User user = new User(id, new Name("Alt"), new EmailAdresse("alt@example.com"), Rolle.ABHOLER);

    Name neuerName = new Name("Neu");
    EmailAdresse neueEmail = new EmailAdresse("neu@example.com");

    user.aendereName(neuerName);
    user.aendereEmail(neueEmail);
    user.aendereRolle(Rolle.ANBIETER);

    assertEquals(neuerName, user.getName());
    assertEquals(neueEmail, user.getEmail());
    assertEquals(Rolle.ANBIETER, user.getRolle());
  }

  @Test
  void aendereNameWirftExceptionBeiNull() {
    User user =
        new User(
            new UserId(UUID.randomUUID()),
            new Name("Alt"),
            new EmailAdresse("alt@example.com"),
            Rolle.ABHOLER);

    assertThrows(NullPointerException.class, () -> user.aendereName(null));
  }

  @Test
  void aendereEmailWirftExceptionBeiNull() {
    User user =
        new User(
            new UserId(UUID.randomUUID()),
            new Name("Alt"),
            new EmailAdresse("alt@example.com"),
            Rolle.ABHOLER);

    assertThrows(NullPointerException.class, () -> user.aendereEmail(null));
  }

  @Test
  void aendereRolleWirftExceptionBeiNull() {
    User user =
        new User(
            new UserId(UUID.randomUUID()),
            new Name("Alt"),
            new EmailAdresse("alt@example.com"),
            Rolle.ABHOLER);

    assertThrows(NullPointerException.class, () -> user.aendereRolle(null));
  }
}
