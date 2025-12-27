package com.foodrescue.userverwaltung.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.userverwaltung.domain.valueobjects.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AnbieterProfilTest {

  private static Adresse adresse() {
    return new Adresse("Ahornstr. 11", "12345", "Berlin", "DE");
  }

  @Test
  void erstellenFuerAnbieter_rejectsNonAnbieterRole() {
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                AnbieterProfil.erstellenFuerAnbieter(
                    AnbieterProfilId.neu(),
                    new UserId(UUID.randomUUID()),
                    Rolle.ABHOLER,
                    new Geschaeftsname("Bäckerei"),
                    Geschaeftstyp.BAECKEREI,
                    adresse(),
                    null));
    assertTrue(ex.getMessage().contains("nur für User mit Rolle ANBIETER"));
  }

  @Test
  void erstellenFuerAnbieter_allowsNullGeoStandort() {
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            AnbieterProfilId.neu(),
            new UserId(UUID.randomUUID()),
            Rolle.ANBIETER,
            new Geschaeftsname("Bäckerei"),
            Geschaeftstyp.BAECKEREI,
            adresse(),
            null);

    assertTrue(profil.getGeoStandort().isEmpty());
  }

  @Test
  void setzeUndEntferneGeoStandort() {
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            AnbieterProfilId.neu(),
            new UserId(UUID.randomUUID()),
            Rolle.ANBIETER,
            new Geschaeftsname("Bäckerei"),
            Geschaeftstyp.BAECKEREI,
            adresse(),
            null);

    profil.setzeGeoStandort(new GeoStandort(52.5, 13.4));
    assertTrue(profil.getGeoStandort().isPresent());

    profil.entferneGeoStandort();
    assertTrue(profil.getGeoStandort().isEmpty());
  }

  @Test
  void aenderungen_rejectNulls() {
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            AnbieterProfilId.neu(),
            new UserId(UUID.randomUUID()),
            Rolle.ANBIETER,
            new Geschaeftsname("Bäckerei"),
            Geschaeftstyp.BAECKEREI,
            adresse(),
            null);

    assertThrows(NullPointerException.class, () -> profil.aendereGeschaeftsname(null));
    assertThrows(NullPointerException.class, () -> profil.aendereGeschaeftstyp(null));
    assertThrows(NullPointerException.class, () -> profil.aendereAdresse(null));
    assertThrows(NullPointerException.class, () -> profil.setzeGeoStandort(null));
  }

  @Test
  void aenderungen_updateState() {
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            AnbieterProfilId.neu(),
            new UserId(UUID.randomUUID()),
            Rolle.ANBIETER,
            new Geschaeftsname("Bäckerei"),
            Geschaeftstyp.BAECKEREI,
            adresse(),
            null);

    profil.aendereGeschaeftsname(new Geschaeftsname("Supermarkt X"));
    profil.aendereGeschaeftstyp(Geschaeftstyp.SUPERMARKT);
    profil.aendereAdresse(new Adresse("Neue Str. 2", "54321", "Hamburg", "DE"));

    assertEquals("Supermarkt X", profil.getGeschaeftsname().getValue());
    assertEquals(Geschaeftstyp.SUPERMARKT, profil.getGeschaeftstyp());
    assertEquals("Hamburg", profil.getAdresse().getOrt());
  }
}
