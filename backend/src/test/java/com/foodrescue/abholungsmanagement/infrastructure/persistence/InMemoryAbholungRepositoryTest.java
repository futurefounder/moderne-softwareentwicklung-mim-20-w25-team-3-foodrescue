package com.foodrescue.abholungsmanagement.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.abholungsmanagement.domain.model.Abholung;
import org.junit.jupiter.api.Test;

class InMemoryAbholungRepositoryTest {

  @Test
  void speichernAndFindeMitId_roundTrip() {
    InMemoryAbholungRepository repo = new InMemoryAbholungRepository();
    Abholung a = new Abholung("a1", "r1", Abholcode.of("AB12"));

    repo.speichern(a);

    assertTrue(repo.findeMitId("a1").isPresent());
    assertSame(a, repo.findeMitId("a1").orElseThrow());
  }

  @Test
  void findeMitId_unknown_returnsEmpty() {
    InMemoryAbholungRepository repo = new InMemoryAbholungRepository();
    assertTrue(repo.findeMitId("does-not-exist").isEmpty());
  }

  @Test
  void speichern_overwritesSameId_lastOneWins() {
    InMemoryAbholungRepository repo = new InMemoryAbholungRepository();
    Abholung a1 = new Abholung("a1", "r1", Abholcode.of("AB12"));
    Abholung a2 = new Abholung("a1", "r2", Abholcode.of("CD34"));

    repo.speichern(a1);
    repo.speichern(a2);

    Abholung loaded = repo.findeMitId("a1").orElseThrow();
    assertSame(a2, loaded);
    assertEquals("r2", loaded.getReservierungsId());
  }
}
