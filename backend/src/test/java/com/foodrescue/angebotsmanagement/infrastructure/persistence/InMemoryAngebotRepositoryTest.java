package com.foodrescue.angebotsmanagement.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryAngebotRepositoryTest {

  private static Angebot angebot(String id, String anbieterId, Angebot.Status status) {
    UserId anbieter = new UserId(UUID.fromString(anbieterId));
    AbholZeitfenster fenster =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0));
    Angebot a = Angebot.erstelle(AngebotsId.of(id), anbieter, "T", "B", Set.of(), fenster);
    if (status != Angebot.Status.ENTWURF) {
      // Move status via domain methods to avoid touching internals
      if (status == Angebot.Status.VERFUEGBAR) {
        a.veroeffentlichen();
      } else if (status == Angebot.Status.RESERVIERT) {
        a.veroeffentlichen();
        a.reservieren(
            "abholer-1", com.foodrescue.abholungsmanagement.domain.model.Abholcode.of("AB12"));
      }
    }
    return a;
  }

  @Test
  void speichernAndFindeMitId_roundTrip() {
    InMemoryAngebotRepository repo = new InMemoryAngebotRepository();
    String anbieterUuid = UUID.randomUUID().toString();
    Angebot a = angebot("a1", anbieterUuid, Angebot.Status.ENTWURF);

    repo.speichern(a);

    assertTrue(repo.findeMitId(AngebotsId.of("a1")).isPresent());
    assertSame(a, repo.findeMitId(AngebotsId.of("a1")).orElseThrow());
  }

  @Test
  void findeMitId_unknown_returnsEmpty() {
    InMemoryAngebotRepository repo = new InMemoryAngebotRepository();
    assertTrue(repo.findeMitId(AngebotsId.of("missing")).isEmpty());
  }

  @Test
  void speichern_overwritesSameId_lastOneWins() {
    InMemoryAngebotRepository repo = new InMemoryAngebotRepository();
    String anbieterUuid = UUID.randomUUID().toString();

    Angebot a1 = angebot("a1", anbieterUuid, Angebot.Status.ENTWURF);
    Angebot a2 = angebot("a1", anbieterUuid, Angebot.Status.VERFUEGBAR);

    repo.speichern(a1);
    repo.speichern(a2);

    Angebot loaded = repo.findeMitId(AngebotsId.of("a1")).orElseThrow();
    assertSame(a2, loaded);
    assertEquals(Angebot.Status.VERFUEGBAR, loaded.getStatus());
  }

  @Test
  void findeAlleVerfuegbar_filtersByStatus() {
    InMemoryAngebotRepository repo = new InMemoryAngebotRepository();
    String anbieterUuid = UUID.randomUUID().toString();

    repo.speichern(angebot("a1", anbieterUuid, Angebot.Status.ENTWURF));
    repo.speichern(angebot("a2", anbieterUuid, Angebot.Status.VERFUEGBAR));
    repo.speichern(angebot("a3", anbieterUuid, Angebot.Status.RESERVIERT));

    List<Angebot> verfuegbar = repo.findeAlleVerfuegbar();
    assertEquals(1, verfuegbar.size());
    assertEquals("a2", verfuegbar.get(0).getId());
  }

  @Test
  void findeFuerAnbieter_filtersByAnbieterId() {
    InMemoryAngebotRepository repo = new InMemoryAngebotRepository();
    String a1 = UUID.randomUUID().toString();
    String a2 = UUID.randomUUID().toString();

    repo.speichern(angebot("x1", a1, Angebot.Status.ENTWURF));
    repo.speichern(angebot("x2", a2, Angebot.Status.ENTWURF));
    repo.speichern(angebot("x3", a1, Angebot.Status.VERFUEGBAR));

    List<Angebot> list = repo.findeFuerAnbieter(a1);
    assertEquals(2, list.size());
    assertTrue(list.stream().allMatch(a -> a.getAnbieterId().equals(a1)));
  }
}
