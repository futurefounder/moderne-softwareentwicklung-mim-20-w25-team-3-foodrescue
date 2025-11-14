package com.foodrescue.anbieterverwaltung.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.anbieterverwaltung.valueobjects.Adresse;
import com.foodrescue.anbieterverwaltung.valueobjects.AnbieterProfilId;
import com.foodrescue.anbieterverwaltung.valueobjects.GeoStandort;
import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftsname;
import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.valueobjects.Rolle;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AnbieterProfilTest {

  private AnbieterProfilId profilId() {
    return new AnbieterProfilId(UUID.randomUUID());
  }

  private UserId userId() {
    return new UserId(UUID.randomUUID());
  }

  private Adresse adresse() {
    return new Adresse("Strasse 1", "12345", "Stadt", "DE");
  }

  @Test
  void erstellenFuerAnbieterMitGueltigenWerten() {
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            profilId(),
            userId(),
            Rolle.ANBIETER,
            new Geschaeftsname("Baeckerei Schmidt"),
            Geschaeftstyp.BAECKEREI,
            adresse(),
            new GeoStandort(50.0, 8.0));

    assertNotNull(profil.getId());
    assertNotNull(profil.getUserId());
    assertEquals("Baeckerei Schmidt", profil.getGeschaeftsname().getValue());
    assertEquals(Geschaeftstyp.BAECKEREI, profil.getGeschaeftstyp());
    assertEquals("Strasse 1", profil.getAdresse().getStrasse());
    assertTrue(profil.getGeoStandort().isPresent());
  }

  @Test
  void erstellenFuerAnbieterWirftExceptionWennRolleNichtAnbieter() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            AnbieterProfil.erstellenFuerAnbieter(
                profilId(),
                userId(),
                Rolle.ABHOLER,
                new Geschaeftsname("Baeckerei Schmidt"),
                Geschaeftstyp.BAECKEREI,
                adresse(),
                null));
  }

  @Test
  void mutatorenAendernWerte() {
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            profilId(),
            userId(),
            Rolle.ANBIETER,
            new Geschaeftsname("Alt"),
            Geschaeftstyp.SUPERMARKT,
            adresse(),
            null);

    Geschaeftsname neuName = new Geschaeftsname("Neu");
    Adresse neueAdresse = new Adresse("Neue Str", "54321", "Neustadt", "DE");
    GeoStandort neuerStandort = new GeoStandort(51.0, 9.0);

    profil.aendereGeschaeftsname(neuName);
    profil.aendereGeschaeftstyp(Geschaeftstyp.RESTAURANT);
    profil.aendereAdresse(neueAdresse);
    profil.setzeGeoStandort(neuerStandort);

    assertEquals(neuName, profil.getGeschaeftsname());
    assertEquals(Geschaeftstyp.RESTAURANT, profil.getGeschaeftstyp());
    assertEquals(neueAdresse, profil.getAdresse());
    assertEquals(neuerStandort, profil.getGeoStandort().orElseThrow());

    profil.entferneGeoStandort();
    assertTrue(profil.getGeoStandort().isEmpty());
  }

  @Test
  void mutatorenWerfenExceptionBeiNullWerten() {
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            profilId(),
            userId(),
            Rolle.ANBIETER,
            new Geschaeftsname("Alt"),
            Geschaeftstyp.SUPERMARKT,
            adresse(),
            null);

    assertThrows(NullPointerException.class, () -> profil.aendereGeschaeftsname(null));
    assertThrows(NullPointerException.class, () -> profil.aendereGeschaeftstyp(null));
    assertThrows(NullPointerException.class, () -> profil.aendereAdresse(null));
    assertThrows(NullPointerException.class, () -> profil.setzeGeoStandort(null));
  }
}
