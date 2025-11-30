package com.foodrescue.userverwaltung.infrastructure.web.rest;

import com.foodrescue.userverwaltung.application.commands.ErstelleAnbieterProfilCommand;
import com.foodrescue.userverwaltung.application.services.AnbieterProfilApplicationService;
import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilDetailsQuery;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilFuerUserQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anbieter-profile")
public class AnbieterProfilController {

  private final AnbieterProfilApplicationService anbieterProfilApplicationService;

  public AnbieterProfilController(
      AnbieterProfilApplicationService anbieterProfilApplicationService) {
    this.anbieterProfilApplicationService = anbieterProfilApplicationService;
  }

  // POST: direkt das Command als RequestBody akzeptieren (Command enthält Value-Objects)
  @PostMapping
  public ResponseEntity<AnbieterProfilResponse> erstelleAnbieterProfil(
      @RequestBody ErstelleAnbieterProfilCommand command) {

    AnbieterProfilDetailsQuery result =
        anbieterProfilApplicationService.erstelleAnbieterProfil(command);

    // Adresse und GeoStandort entpacken und korrekte Parameter an den Response-Konstruktor
    // übergeben
    String strasse = result.getAdresse().getStrasse();
    String plz = result.getAdresse().getPlz();
    String ort = result.getAdresse().getOrt();
    String land = result.getAdresse().getLand();

    Double lat = null;
    Double lon = null;
    if (result.getGeoStandort() != null) {
      lat = result.getGeoStandort().getBreitengrad();
      lon = result.getGeoStandort().getLaengengrad();
    }

    AnbieterProfilResponse response =
        new AnbieterProfilResponse(
            result.getId().getValue(),
            result.getUserId().getValue(),
            result.getGeschaeftsname().getValue(),
            result.getGeschaeftstyp(),
            strasse,
            plz,
            ort,
            land,
            lat,
            lon);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // GET: userId aus Pfad als UUID parsen und Service korrekt nutzen
  @GetMapping("/user/{userId}")
  public ResponseEntity<AnbieterProfilResponse> holeProfilFuerUser(@PathVariable String userId) {

    UserId uid = new UserId(UUID.fromString(userId));

    AnbieterProfilFuerUserQuery query =
        anbieterProfilApplicationService.findeAnbieterProfilFuerUser(uid);

    // Korrektur: getAnbieterProfil() liefert ein Domain-Model (AnbieterProfil)
    AnbieterProfil profil = query.getAnbieterProfil();

    String strasse = profil.getAdresse().getStrasse();
    String plz = profil.getAdresse().getPlz();
    String ort = profil.getAdresse().getOrt();
    String land = profil.getAdresse().getLand();

    Double lat = profil.getGeoStandort().map(g -> g.getBreitengrad()).orElse(null);
    Double lon = profil.getGeoStandort().map(g -> g.getLaengengrad()).orElse(null);

    AnbieterProfilResponse response =
        new AnbieterProfilResponse(
            profil.getId().getValue(),
            profil.getUserId().getValue(),
            profil.getGeschaeftsname().getValue(),
            profil.getGeschaeftstyp(),
            strasse,
            plz,
            ort,
            land,
            lat,
            lon);

    return ResponseEntity.ok(response);
  }
}
