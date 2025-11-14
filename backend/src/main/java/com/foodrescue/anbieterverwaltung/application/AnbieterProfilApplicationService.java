package com.foodrescue.anbieterverwaltung.application;

import static jakarta.transaction.Transactional.TxType.SUPPORTS;

import com.foodrescue.anbieterverwaltung.commands.ErstelleAnbieterProfilCommand;
import com.foodrescue.anbieterverwaltung.domain.AnbieterProfil;
import com.foodrescue.anbieterverwaltung.queries.AnbieterProfilDetailsQuery;
import com.foodrescue.anbieterverwaltung.queries.AnbieterProfilFuerUserQuery;
import com.foodrescue.anbieterverwaltung.repositories.AnbieterProfilRepository;
import com.foodrescue.anbieterverwaltung.valueobjects.Adresse;
import com.foodrescue.anbieterverwaltung.valueobjects.AnbieterProfilId;
import com.foodrescue.anbieterverwaltung.valueobjects.GeoStandort;
import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftsname;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional // Standard: REQUIRED für schreibende Methoden
public class AnbieterProfilApplicationService {

  private final AnbieterProfilRepository anbieterProfilRepository;

  public AnbieterProfilApplicationService(AnbieterProfilRepository anbieterProfilRepository) {
    this.anbieterProfilRepository = anbieterProfilRepository;
  }

  /** Schreibender Use Case – neues AnbieterProfil anlegen. */
  public AnbieterProfilId erstelleAnbieterProfil(ErstelleAnbieterProfilCommand command) {
    AnbieterProfilId profilId = AnbieterProfilId.neu();

    Adresse adresse =
        new Adresse(command.getStrasse(), command.getPlz(), command.getOrt(), command.getLand());

    GeoStandort geoStandort = null;
    if (command.hatGeoStandort()) {
      geoStandort = new GeoStandort(command.getBreitengrad(), command.getLaengengrad());
    }

    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            profilId,
            new UserId(command.getUserId()),
            command.getUserRolle(),
            new Geschaeftsname(command.getGeschaeftsname()),
            command.getGeschaeftstyp(),
            adresse,
            geoStandort);

    anbieterProfilRepository.speichern(profil);
    return profilId;
  }

  /** Lesender Use Case – Profil über ProfilId. */
  @Transactional(SUPPORTS)
  public AnbieterProfil holeProfil(AnbieterProfilDetailsQuery query) {
    AnbieterProfilId id = new AnbieterProfilId(query.getProfilId());
    return anbieterProfilRepository
        .findeMitId(id)
        .orElseThrow(
            () -> new NoSuchElementException("AnbieterProfil mit Id " + id + " nicht gefunden"));
  }

  /** Lesender Use Case – Profil über UserId. */
  @Transactional(SUPPORTS)
  public AnbieterProfil holeProfilFuerUser(AnbieterProfilFuerUserQuery query) {
    UserId userId = new UserId(query.getUserId());
    return anbieterProfilRepository
        .findeFuerUser(userId)
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    "AnbieterProfil für User " + userId + " nicht gefunden"));
  }

  // Convenience-Methoden mit direkter UUID

  @Transactional(SUPPORTS)
  public AnbieterProfil holeProfil(UUID profilId) {
    return holeProfil(new AnbieterProfilDetailsQuery(profilId));
  }

  @Transactional(SUPPORTS)
  public AnbieterProfil holeProfilFuerUser(UUID userId) {
    return holeProfilFuerUser(new AnbieterProfilFuerUserQuery(userId));
  }
}
