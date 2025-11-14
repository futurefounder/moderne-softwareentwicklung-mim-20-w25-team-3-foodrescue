package com.foodrescue.anbieterverwaltung.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.anbieterverwaltung.commands.ErstelleAnbieterProfilCommand;
import com.foodrescue.anbieterverwaltung.domain.AnbieterProfil;
import com.foodrescue.anbieterverwaltung.queries.AnbieterProfilDetailsQuery;
import com.foodrescue.anbieterverwaltung.queries.AnbieterProfilFuerUserQuery;
import com.foodrescue.anbieterverwaltung.repositories.AnbieterProfilRepository;
import com.foodrescue.anbieterverwaltung.valueobjects.Adresse;
import com.foodrescue.anbieterverwaltung.valueobjects.AnbieterProfilId;
import com.foodrescue.anbieterverwaltung.valueobjects.GeoStandort;
import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftsname;
import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.valueobjects.Rolle;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AnbieterProfilApplicationServiceTest {

  @Mock private AnbieterProfilRepository anbieterProfilRepository;

  @InjectMocks private AnbieterProfilApplicationService applicationService;

  @Test
  void erstelleAnbieterProfilSpeichertProfilImRepository() {
    UUID userUuid = UUID.randomUUID();
    ErstelleAnbieterProfilCommand command =
        new ErstelleAnbieterProfilCommand(
            userUuid,
            Rolle.ANBIETER,
            "Baeckerei Schmidt",
            Geschaeftstyp.BAECKEREI,
            "Strasse 1",
            "12345",
            "Stadt",
            "DE",
            50.0,
            8.0);

    ArgumentCaptor<AnbieterProfil> profilCaptor = ArgumentCaptor.forClass(AnbieterProfil.class);

    AnbieterProfilId id = applicationService.erstelleAnbieterProfil(command);

    assertNotNull(id);
    verify(anbieterProfilRepository, times(1)).speichern(profilCaptor.capture());

    AnbieterProfil profil = profilCaptor.getValue();
    assertEquals("Baeckerei Schmidt", profil.getGeschaeftsname().getValue());
    assertEquals(Geschaeftstyp.BAECKEREI, profil.getGeschaeftstyp());
    assertEquals("Strasse 1", profil.getAdresse().getStrasse());
    assertTrue(profil.getGeoStandort().isPresent());
    assertEquals(userUuid, profil.getUserId().getValue());
  }

  @Test
  void erstelleAnbieterProfilOhneGeoStandortIstErlaubt() {
    UUID userUuid = UUID.randomUUID();
    ErstelleAnbieterProfilCommand command =
        new ErstelleAnbieterProfilCommand(
            userUuid,
            Rolle.ANBIETER,
            "Baeckerei Schmidt",
            Geschaeftstyp.BAECKEREI,
            "Strasse 1",
            "12345",
            "Stadt",
            "DE",
            null,
            null);

    ArgumentCaptor<AnbieterProfil> profilCaptor = ArgumentCaptor.forClass(AnbieterProfil.class);

    applicationService.erstelleAnbieterProfil(command);

    verify(anbieterProfilRepository, times(1)).speichern(profilCaptor.capture());
    AnbieterProfil profil = profilCaptor.getValue();

    assertTrue(profil.getGeoStandort().isEmpty());
  }

  @Test
  void holeProfilGibtGefundenesProfilZurueck() {
    UUID profilUuid = UUID.randomUUID();
    AnbieterProfilId profilId = new AnbieterProfilId(profilUuid);

    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            profilId,
            new UserId(UUID.randomUUID()),
            Rolle.ANBIETER,
            new Geschaeftsname("Baeckerei"),
            Geschaeftstyp.BAECKEREI,
            new Adresse("Strasse 1", "12345", "Stadt", "DE"),
            new GeoStandort(50.0, 8.0));

    when(anbieterProfilRepository.findeMitId(profilId)).thenReturn(Optional.of(profil));

    AnbieterProfil result =
        applicationService.holeProfil(new AnbieterProfilDetailsQuery(profilUuid));

    assertEquals(profil, result);
    verify(anbieterProfilRepository, times(1)).findeMitId(profilId);
  }

  @Test
  void holeProfilWirftExceptionWennNichtGefunden() {
    UUID profilUuid = UUID.randomUUID();
    AnbieterProfilId profilId = new AnbieterProfilId(profilUuid);

    when(anbieterProfilRepository.findeMitId(profilId)).thenReturn(Optional.empty());

    assertThrows(
        NoSuchElementException.class,
        () -> applicationService.holeProfil(new AnbieterProfilDetailsQuery(profilUuid)));
  }

  @Test
  void holeProfilFuerUserGibtGefundenesProfilZurueck() {
    UUID userUuid = UUID.randomUUID();
    UserId userId = new UserId(userUuid);

    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            AnbieterProfilId.neu(),
            userId,
            Rolle.ANBIETER,
            new Geschaeftsname("Baeckerei"),
            Geschaeftstyp.BAECKEREI,
            new Adresse("Strasse 1", "12345", "Stadt", "DE"),
            null);

    when(anbieterProfilRepository.findeFuerUser(userId)).thenReturn(Optional.of(profil));

    AnbieterProfil result =
        applicationService.holeProfilFuerUser(new AnbieterProfilFuerUserQuery(userUuid));

    assertEquals(profil, result);
    verify(anbieterProfilRepository, times(1)).findeFuerUser(userId);
  }

  @Test
  void holeProfilFuerUserWirftExceptionWennNichtGefunden() {
    UUID userUuid = UUID.randomUUID();
    UserId userId = new UserId(userUuid);

    when(anbieterProfilRepository.findeFuerUser(userId)).thenReturn(Optional.empty());

    assertThrows(
        NoSuchElementException.class,
        () -> applicationService.holeProfilFuerUser(new AnbieterProfilFuerUserQuery(userUuid)));
  }
}
