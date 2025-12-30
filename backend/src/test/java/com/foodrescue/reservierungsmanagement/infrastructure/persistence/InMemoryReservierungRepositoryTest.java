package com.foodrescue.reservierungsmanagement.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import org.junit.jupiter.api.Test;

class InMemoryReservierungRepositoryTest {

  @Test
  void speichernAndFindeMitId_roundTrip() {
    InMemoryReservierungRepository repo = new InMemoryReservierungRepository();
    Reservierung r =
        Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", Abholcode.of("AB12"));

    repo.speichern(r);

    assertTrue(repo.findeMitId("r1").isPresent());
    assertSame(r, repo.findeMitId("r1").orElseThrow());
  }

  @Test
  void findeMitId_unknown_returnsEmpty() {
    InMemoryReservierungRepository repo = new InMemoryReservierungRepository();
    assertTrue(repo.findeMitId("missing").isEmpty());
  }

  @Test
  void speichern_overwritesSameId_lastWins() {
    InMemoryReservierungRepository repo = new InMemoryReservierungRepository();
    Reservierung r1 =
        Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", Abholcode.of("AB12"));
    Reservierung r2 =
        Reservierung.erstelle(new ReservierungsId("r1"), "a2", "u2", Abholcode.of("CD34"));

    repo.speichern(r1);
    repo.speichern(r2);

    Reservierung loaded = repo.findeMitId("r1").orElseThrow();
    assertSame(r2, loaded);
    assertEquals("a2", loaded.getAngebotId());
    assertEquals("u2", loaded.getAbholerId());
  }

  @Test
  void findeFuerAbholer_filtersByAbholerId() {
    InMemoryReservierungRepository repo = new InMemoryReservierungRepository();

    Reservierung u1r1 =
        Reservierung.erstelle(new ReservierungsId("r1"), "a1", "u1", Abholcode.of("AB12"));
    Reservierung u1r2 =
        Reservierung.erstelle(new ReservierungsId("r2"), "a2", "u1", Abholcode.of("CD34"));
    Reservierung u2r1 =
        Reservierung.erstelle(new ReservierungsId("r3"), "a3", "u2", Abholcode.of("EF56"));

    repo.speichern(u1r1);
    repo.speichern(u1r2);
    repo.speichern(u2r1);

    var list = repo.findeFuerAbholer("u1");
    assertEquals(2, list.size());
    assertTrue(list.stream().allMatch(r -> r.getAbholerId().equals("u1")));
  }
}
