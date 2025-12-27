package com.foodrescue.userverwaltung.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.valueobjects.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryAnbieterProfilRepositoryTest {

  private static AnbieterProfil profil(UserId uid, String name) {
    return AnbieterProfil.erstellenFuerAnbieter(
        AnbieterProfilId.neu(),
        uid,
        Rolle.ANBIETER,
        new Geschaeftsname(name),
        Geschaeftstyp.SUPERMARKT,
        new Adresse("Str. 1", "12345", "Ort", "DE"),
        null);
  }

  @Test
  void speichernUndFindenMitId() {
    InMemoryAnbieterProfilRepository repo = new InMemoryAnbieterProfilRepository();
    UserId uid = new UserId(UUID.randomUUID());
    AnbieterProfil p = profil(uid, "Shop");

    repo.speichern(p);

    assertTrue(repo.findeMitId(p.getId()).isPresent());
    assertEquals(uid, repo.findeMitId(p.getId()).orElseThrow().getUserId());
  }

  @Test
  void findeFuerUser_returnsEmptyWhenMissing() {
    InMemoryAnbieterProfilRepository repo = new InMemoryAnbieterProfilRepository();
    assertTrue(repo.findeFuerUser(new UserId(UUID.randomUUID())).isEmpty());
  }

  @Test
  void edgeCase_userIndexOnlyStoresLastProfilForUser() {
    InMemoryAnbieterProfilRepository repo = new InMemoryAnbieterProfilRepository();
    UserId uid = new UserId(UUID.randomUUID());

    AnbieterProfil p1 = profil(uid, "Shop 1");
    AnbieterProfil p2 = profil(uid, "Shop 2");

    repo.speichern(p1);
    repo.speichern(p2);

    AnbieterProfil found = repo.findeFuerUser(uid).orElseThrow();
    assertEquals(p2.getId(), found.getId());
    assertEquals("Shop 2", found.getGeschaeftsname().getValue());
  }
}
