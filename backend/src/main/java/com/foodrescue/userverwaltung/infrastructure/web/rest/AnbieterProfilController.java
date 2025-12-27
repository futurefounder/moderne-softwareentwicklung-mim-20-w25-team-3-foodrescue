package com.foodrescue.userverwaltung.infrastructure.web.rest;

import com.foodrescue.userverwaltung.application.commands.ErstelleAnbieterProfilCommand;
import com.foodrescue.userverwaltung.application.services.AnbieterProfilApplicationService;
import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilDetailsQuery;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilFuerUserQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.Adresse;
import com.foodrescue.userverwaltung.domain.valueobjects.GeoStandort;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftsname;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

  /**
   * FIX: Wir akzeptieren nicht das Command als JSON (enthält ValueObjects), sondern ein Request-DTO
   * und mappen im Controller auf das Command.
   */
  @PostMapping
  public ResponseEntity<AnbieterProfilResponse> erstelleAnbieterProfil(
      @RequestBody ErstelleAnbieterProfilRequest request) {

    UserId userId = new UserId(UUID.fromString(request.getUserId()));
    Geschaeftsname geschaeftsname = new Geschaeftsname(request.getGeschaeftsname());
    Geschaeftstyp geschaeftstyp = Geschaeftstyp.valueOf(request.getGeschaeftstyp());

    Adresse adresse = baueAdresse(request);

    GeoStandort geoStandort = null;
    if (request.getBreitengrad() != null || request.getLaengengrad() != null) {
      if (request.getBreitengrad() == null || request.getLaengengrad() == null) {
        throw new IllegalArgumentException(
            "GeoStandort: Bitte Breitengrad und Längengrad entweder beide setzen oder beide leer lassen.");
      }
      geoStandort = new GeoStandort(request.getBreitengrad(), request.getLaengengrad());
    }

    ErstelleAnbieterProfilCommand command =
        new ErstelleAnbieterProfilCommand(
            userId, geschaeftsname, geschaeftstyp, adresse, geoStandort);

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
  public ResponseEntity<AnbieterProfilResponse> holeProfilFuerUser(
      @PathVariable("userId") String userId) {

    UserId uid = new UserId(UUID.fromString(userId));

    AnbieterProfilFuerUserQuery query =
        anbieterProfilApplicationService.findeAnbieterProfilFuerUser(uid);

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

  private Adresse baueAdresse(ErstelleAnbieterProfilRequest request) {
    // Bevorzugt: strukturierte Felder (empfohlen)
    if (notBlank(request.getStrasse())
        && notBlank(request.getPlz())
        && notBlank(request.getOrt())
        && notBlank(request.getLand())) {

      return new Adresse(
          request.getStrasse(), request.getPlz(), request.getOrt(), request.getLand());
    }

    // Fallback: legacy "adresse" String parsen.
    // Erwartetes Format: "Straße Hausnr, PLZ Ort, Land"
    // Beispiel: "Ahornstraße 11, 12345 Berlin, DE"
    String raw = request.getAdresse();
    if (!notBlank(raw)) {
      throw new IllegalArgumentException(
          "Adresse fehlt. Bitte entweder strasse/plz/ort/land setzen oder adresse im Format 'Straße Hausnr, PLZ Ort, Land' senden.");
    }

    Pattern p =
        Pattern.compile("^\\s*(.+?)\\s*,\\s*(\\d{4,6})\\s+(.+?)\\s*,\\s*([A-Za-z]{2,})\\s*$");
    Matcher m = p.matcher(raw);
    if (!m.matches()) {
      throw new IllegalArgumentException(
          "Adresse-Format ungültig. Erwartet: 'Straße Hausnr, PLZ Ort, Land' (z.B. 'Ahornstraße 11, 12345 Berlin, DE').");
    }

    String strasse = m.group(1);
    String plz = m.group(2);
    String ort = m.group(3);
    String land = m.group(4);

    return new Adresse(strasse, plz, ort, land);
  }

  private boolean notBlank(String s) {
    return s != null && !s.trim().isEmpty();
  }
}
