package com.foodrescue.userverwaltung.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.foodrescue.userverwaltung.application.commands.ErstelleAnbieterProfilCommand;
import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilDetailsQuery;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilFuerUserQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.*;
import com.foodrescue.userverwaltung.infrastructure.repositories.AnbieterProfilRepository;
import com.foodrescue.userverwaltung.infrastructure.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnbieterProfilApplicationServiceTest {

  @Mock private AnbieterProfilRepository anbieterProfilRepository;
  @Mock private UserRepository userRepository;
  @Mock private AnbieterDomainService anbieterDomainService;

  @InjectMocks private AnbieterProfilApplicationService service;

  private static Adresse adresse() {
    return new Adresse("Str. 1", "12345", "Ort", "DE");
  }

  @Test
  void erstelleAnbieterProfil_throwsWhenUserMissing() {
    when(userRepository.findeMitId(any(UserId.class))).thenReturn(Optional.empty());

    ErstelleAnbieterProfilCommand cmd =
        new ErstelleAnbieterProfilCommand(
            new UserId(UUID.randomUUID()),
            new Geschaeftsname("Shop"),
            Geschaeftstyp.SUPERMARKT,
            adresse(),
            null);

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> service.erstelleAnbieterProfil(cmd));
    assertTrue(ex.getMessage().contains("User nicht gefunden"));

    verify(anbieterDomainService, never())
        .erstelleAnbieterProfil(any(), any(), any(), any(), any(), any(), any());
    verify(anbieterProfilRepository, never()).speichern(any());
  }

  @Test
  void erstelleAnbieterProfil_success_allowsNullGeoStandort() {
    UserId uid = new UserId(UUID.randomUUID());
    User user =
        User.registrieren(
            uid, new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ANBIETER);

    when(userRepository.findeMitId(uid)).thenReturn(Optional.of(user));

    when(anbieterDomainService.erstelleAnbieterProfil(
            any(), eq(uid), eq(Rolle.ANBIETER), any(), any(), any(), isNull()))
        .thenAnswer(
            inv ->
                AnbieterProfil.erstellenFuerAnbieter(
                    inv.getArgument(0),
                    inv.getArgument(1),
                    inv.getArgument(2),
                    inv.getArgument(3),
                    inv.getArgument(4),
                    inv.getArgument(5),
                    null));

    when(anbieterProfilRepository.speichern(any(AnbieterProfil.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    ErstelleAnbieterProfilCommand cmd =
        new ErstelleAnbieterProfilCommand(
            uid, new Geschaeftsname("Shop"), Geschaeftstyp.SUPERMARKT, adresse(), null);

    AnbieterProfilDetailsQuery q = service.erstelleAnbieterProfil(cmd);

    assertEquals(uid, q.getUserId());
    assertEquals("Shop", q.getGeschaeftsname().getValue());
    assertEquals(Geschaeftstyp.SUPERMARKT, q.getGeschaeftstyp());
    assertNotNull(q.getId());
    assertNull(q.getGeoStandort());

    verify(anbieterProfilRepository).speichern(any(AnbieterProfil.class));
  }

  @Test
  void findeAnbieterProfilFuerUser_throwsWhenProfilMissing() {
    UserId uid = new UserId(UUID.randomUUID());
    User user =
        User.registrieren(
            uid, new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ANBIETER);

    when(userRepository.findeMitId(uid)).thenReturn(Optional.of(user));
    when(anbieterProfilRepository.findeFuerUser(uid)).thenReturn(Optional.empty());

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class, () -> service.findeAnbieterProfilFuerUser(uid));
    assertTrue(ex.getMessage().contains("AnbieterProfil nicht gefunden"));
  }

  @Test
  void findeAnbieterProfilFuerUser_returnsCombinedQuery() {
    UserId uid = new UserId(UUID.randomUUID());
    User user =
        User.registrieren(
            uid, new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ANBIETER);

    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            AnbieterProfilId.neu(),
            uid,
            Rolle.ANBIETER,
            new Geschaeftsname("Shop"),
            Geschaeftstyp.SUPERMARKT,
            adresse(),
            new GeoStandort(52.5, 13.4));

    when(userRepository.findeMitId(uid)).thenReturn(Optional.of(user));
    when(anbieterProfilRepository.findeFuerUser(uid)).thenReturn(Optional.of(profil));

    AnbieterProfilFuerUserQuery q = service.findeAnbieterProfilFuerUser(uid);

    assertEquals(user, q.getUser());
    assertEquals(profil, q.getAnbieterProfil());
  }
}
