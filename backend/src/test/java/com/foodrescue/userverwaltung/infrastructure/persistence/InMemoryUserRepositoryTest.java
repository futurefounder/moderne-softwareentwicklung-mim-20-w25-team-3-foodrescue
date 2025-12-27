package com.foodrescue.userverwaltung.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.valueobjects.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryUserRepositoryTest {

  @Test
  void speichernUndFindenMitId() {
    InMemoryUserRepository repo = new InMemoryUserRepository();
    User u =
        User.registrieren(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);

    repo.speichern(u);

    assertTrue(repo.findeMitId(u.getId()).isPresent());
    assertEquals(u.getEmail(), repo.findeMitId(u.getId()).orElseThrow().getEmail());
  }

  @Test
  void findenMitEmail_isCaseInsensitiveBecauseValueObject() {
    InMemoryUserRepository repo = new InMemoryUserRepository();
    User u =
        User.registrieren(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("Max@Example.com"),
            Rolle.ABHOLER);

    repo.speichern(u);

    assertTrue(repo.findeMitEmail(new EmailAdresse("max@example.com")).isPresent());
  }

  @Test
  void edgeCase_emailIndexIsNotCleanedUpOnEmailChange() {
    // Dokumentiert das aktuelle Verhalten: beim erneuten Speichern mit anderer Email
    // bleibt der alte Email-Key in der Map erhalten.
    InMemoryUserRepository repo = new InMemoryUserRepository();
    UserId id = new UserId(UUID.randomUUID());

    User u =
        User.registrieren(id, new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ABHOLER);
    repo.speichern(u);

    u.aendereEmail(new EmailAdresse("max2@example.com"));
    repo.speichern(u);

    assertTrue(repo.findeMitEmail(new EmailAdresse("max2@example.com")).isPresent());
    // In der aktuellen Implementierung ist auch die alte Adresse weiterhin auffindbar.
    assertTrue(repo.findeMitEmail(new EmailAdresse("max@example.com")).isPresent());
  }
}
