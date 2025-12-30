package com.foodrescue.angebotsmanagement.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

class InMemoryAngebotRepositoryTest {

  private InMemoryAngebotRepository repo;
  private ApplicationEventPublisher mockEventPublisher;

  @BeforeEach
  void setUp() {
    // Mock Event Publisher - macht nichts, da In-Memory Test
    mockEventPublisher = event -> {}; // NoOp Lambda
    repo = new InMemoryAngebotRepository(mockEventPublisher);
  }

  @Test
  void speichernAndFindeMitId_roundTrip() {
    String anbieterUuid = UUID.randomUUID().toString();
    UserId anbieterId = new UserId(UUID.fromString(anbieterUuid));
    Angebot a = angebot("a1", anbieterUuid, Angebot.Status.ENTWURF);

    repo.speichern(a);

    assertTrue(repo.findeMitId(AngebotsId.of("a1")).isPresent());
    assertSame(a, repo.findeMitId(AngebotsId.of("a1")).orElseThrow());
  }

  @Test
  void findeMitId_unknown_returnsEmpty() {
    assertTrue(repo.findeMitId(AngebotsId.of("missing")).isEmpty());
  }

  @Test
  void findeAlleVerfuegbar_filtersStatus() {
    String anbieterUuid = UUID.randomUUID().toString();

    Angebot entwurf = angebot("a1", anbieterUuid, Angebot.Status.ENTWURF);
    Angebot verfuegbar1 = angebot("a2", anbieterUuid, Angebot.Status.VERFUEGBAR);
    Angebot verfuegbar2 = angebot("a3", anbieterUuid, Angebot.Status.VERFUEGBAR);
    Angebot reserviert = angebot("a4", anbieterUuid, Angebot.Status.RESERVIERT);

    repo.speichern(entwurf);
    repo.speichern(verfuegbar1);
    repo.speichern(verfuegbar2);
    repo.speichern(reserviert);

    var list = repo.findeAlleVerfuegbar();
    assertEquals(2, list.size());
    assertTrue(list.stream().allMatch(a -> a.getStatus() == Angebot.Status.VERFUEGBAR));
  }

  @Test
  void findeFuerAnbieter_filtersByAnbieterId() {
    String anbieter1Uuid = UUID.randomUUID().toString();
    String anbieter2Uuid = UUID.randomUUID().toString();

    Angebot a1 = angebot("a1", anbieter1Uuid, Angebot.Status.ENTWURF);
    Angebot a2 = angebot("a2", anbieter1Uuid, Angebot.Status.VERFUEGBAR);
    Angebot a3 = angebot("a3", anbieter2Uuid, Angebot.Status.ENTWURF);

    repo.speichern(a1);
    repo.speichern(a2);
    repo.speichern(a3);

    var list = repo.findeFuerAnbieter(anbieter1Uuid);
    assertEquals(2, list.size());
    assertTrue(
        list.stream().allMatch(a -> a.getAnbieterId().getValue().toString().equals(anbieter1Uuid)));
  }

  // Helper-Methode zum Erstellen von Test-Angeboten
  private Angebot angebot(String id, String anbieterId, Angebot.Status status) {
    AngebotsId angebotsId = AngebotsId.of(id);
    UserId userId = new UserId(UUID.fromString(anbieterId));

    LocalDateTime von = LocalDateTime.now().plusHours(1);
    LocalDateTime bis = von.plusHours(2);
    AbholZeitfenster zeitfenster = new AbholZeitfenster(von, bis);

    Angebot angebot =
        Angebot.erstelle(
            angebotsId,
            userId,
            "Test Angebot " + id,
            "Beschreibung für " + id,
            Set.of("test"),
            zeitfenster);

    // Status setzen falls nötig
    if (status == Angebot.Status.VERFUEGBAR) {
      angebot.veroeffentlichen();
    }
    // Für RESERVIERT müsste man reservieren() aufrufen,
    // aber das brauchen wir hier nicht

    // Events clearen für saubere Tests
    angebot.clearDomainEvents();

    return angebot;
  }
}
