package com.foodrescue.anbieterverwaltung.rest;

import com.foodrescue.anbieterverwaltung.application.AnbieterProfilApplicationService;
import com.foodrescue.anbieterverwaltung.commands.ErstelleAnbieterProfilCommand;
import com.foodrescue.anbieterverwaltung.domain.AnbieterProfil;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anbieterprofile")
public class AnbieterProfilController {

  private final AnbieterProfilApplicationService applicationService;

  public AnbieterProfilController(AnbieterProfilApplicationService applicationService) {
    this.applicationService = applicationService;
  }

  @PostMapping
  public ResponseEntity<AnbieterProfilResponse> erstelleProfil(
      @RequestBody ErstelleAnbieterProfilRequest request) {
    ErstelleAnbieterProfilCommand command =
        new ErstelleAnbieterProfilCommand(
            request.getUserId(),
            request.getUserRolle(),
            request.getGeschaeftsname(),
            request.getGeschaeftstyp(),
            request.getStrasse(),
            request.getPlz(),
            request.getOrt(),
            request.getLand(),
            request.getBreitengrad(),
            request.getLaengengrad());

    UUID profilId = applicationService.erstelleAnbieterProfil(command).getValue();
    AnbieterProfil profil = applicationService.holeProfil(profilId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(AnbieterProfilResponse.fromDomain(profil));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AnbieterProfilResponse> holeProfil(@PathVariable("id") UUID id) {
    AnbieterProfil profil = applicationService.holeProfil(id);
    return ResponseEntity.ok(AnbieterProfilResponse.fromDomain(profil));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<AnbieterProfilResponse> holeProfilFuerUser(
      @PathVariable("userId") UUID userId) {
    AnbieterProfil profil = applicationService.holeProfilFuerUser(userId);
    return ResponseEntity.ok(AnbieterProfilResponse.fromDomain(profil));
  }
}
