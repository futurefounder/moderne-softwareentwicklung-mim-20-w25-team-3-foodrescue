package com.foodrescue.userverwaltung.application.services;

import com.foodrescue.userverwaltung.application.commands.ErstelleAnbieterProfilCommand;
import com.foodrescue.userverwaltung.domain.events.AnbieterProfilErstelltEvent;
import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilDetailsQuery;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilFuerUserQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.Adresse;
import com.foodrescue.userverwaltung.domain.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.domain.valueobjects.GeoStandort;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftsname;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import com.foodrescue.userverwaltung.infrastructure.repositories.AnbieterProfilRepository;
import com.foodrescue.userverwaltung.infrastructure.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AnbieterProfilApplicationService {

  private final AnbieterProfilRepository anbieterProfilRepository;
  private final UserRepository userRepository;
  private final AnbieterDomainService anbieterDomainService;

  public AnbieterProfilApplicationService(
      AnbieterProfilRepository anbieterProfilRepository,
      UserRepository userRepository,
      AnbieterDomainService anbieterDomainService) {
    this.anbieterProfilRepository = anbieterProfilRepository;
    this.userRepository = userRepository;
    this.anbieterDomainService = anbieterDomainService;
  }

  public AnbieterProfilDetailsQuery erstelleAnbieterProfil(ErstelleAnbieterProfilCommand command) {

    // Command liefert bereits Value-Objects
    UserId userId = command.getUserId();
    User user =
        userRepository
            .findeMitId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));

    AnbieterProfilId profilId = AnbieterProfilId.neu();

    Geschaeftsname geschaeftsname = command.getGeschaeftsname();
    Geschaeftstyp geschaeftstyp = command.getGeschaeftstyp();
    Adresse adresse = command.getAdresse();
    // GeoStandort darf null sein — keine String-Methoden darauf aufrufen
    GeoStandort geoStandort = command.getGeoStandort();

    AnbieterProfil profil =
        anbieterDomainService.erstelleAnbieterProfil(
            profilId,
            user.getId(),
            user.getRolle(),
            geschaeftsname,
            geschaeftstyp,
            adresse,
            geoStandort);

    AnbieterProfil gespeichertesProfil = anbieterProfilRepository.speichern(profil);

    AnbieterProfilErstelltEvent event =
        new AnbieterProfilErstelltEvent(
            gespeichertesProfil.getId(), gespeichertesProfil.getUserId());
    // domainEventPublisher.publish(event);

    return new AnbieterProfilDetailsQuery(
        gespeichertesProfil.getId(),
        gespeichertesProfil.getUserId(),
        gespeichertesProfil.getGeschaeftsname(),
        gespeichertesProfil.getGeschaeftstyp(),
        gespeichertesProfil.getAdresse(),
        gespeichertesProfil.getGeoStandort().orElse(null));
  }

  public AnbieterProfilFuerUserQuery findeAnbieterProfilFuerUser(UserId userId) {
    User user =
        userRepository
            .findeMitId(userId)
            .orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));

    AnbieterProfil profil =
        anbieterProfilRepository
            .findeFuerUser(userId)
            .orElseThrow(() -> new IllegalArgumentException("AnbieterProfil nicht gefunden"));

    return new AnbieterProfilFuerUserQuery(user, profil);
  }
}
