package com.foodrescue.angebotsmanagement.infrastructure.web.rest;

import com.foodrescue.angebotsmanagement.application.commands.ErstelleAngebotCommand;
import com.foodrescue.angebotsmanagement.application.commands.VeroeffentlicheAngebotCommand;
import com.foodrescue.angebotsmanagement.application.services.AngebotApplicationService;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.web.rest.mapper.AngebotMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/angebote", produces = MediaType.APPLICATION_JSON_VALUE)
public class AngebotController {

    private final AngebotApplicationService service;
    private final AngebotMapper mapper;

    public AngebotController(AngebotApplicationService service, AngebotMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * Erstellt ein neues Angebot als ENTWURF (nicht veröffentlicht).
     * Request: JSON (anbieterId, titel, beschreibung, tags, zeitfenster{von,bis})
     * Response: JSON { id: "..." }
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AngebotMapper.CreateAngebotResponse> erstelleAngebot(
            @RequestBody AngebotMapper.ErstelleAngebotRequest request
    ) {
        ErstelleAngebotCommand command = new ErstelleAngebotCommand();
        command.setAnbieterId(request.getAnbieterId());
        command.setTitel(request.getTitel());
        command.setBeschreibung(request.getBeschreibung());
        command.setTags(request.getTags());

        if (request.getZeitfenster() != null) {
            command.setVon(request.getZeitfenster().getVon());
            command.setBis(request.getZeitfenster().getBis());
        }

        AngebotsId id = service.erstelleAngebot(command);

        // Wichtig: NICHT AngebotsId (Domain) zurückgeben, sondern DTO/primitive
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AngebotMapper.CreateAngebotResponse(id.value()));
    }

    /**
     * Veröffentlicht ein bestehendes Angebot (Statuswechsel).
     */
    @PostMapping(value = "/{id}/veroeffentlichen", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> veroeffentlicheAngebot(@PathVariable("id") String id) {
        service.veroeffentlicheAngebot(new VeroeffentlicheAngebotCommand(new AngebotsId(id)));
        return ResponseEntity.ok().build();
    }

    /**
     * Liefert alle veröffentlicht/verfügbaren Angebote (für Abholer-Suche).
     * Response: Liste von AngebotResponse (DTO)
     */
    @GetMapping("/verfuegbar")
    public ResponseEntity<List<AngebotMapper.AngebotResponse>> findeVerfuegbareAngebote() {
        return ResponseEntity.ok(mapper.toResponseList(service.findeVerfuegbareAngebote()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AngebotMapper.AngebotResponse> findeAngebotDetails(@PathVariable("id") String id) {
        Angebot angebot = service.findeAngebotMitId(new AngebotsId(id));
        return ResponseEntity.ok(mapper.toResponse(angebot));
    }


    /**
     * Liefert alle Angebote eines Anbieters.
     * Response: Liste von AngebotResponse (DTO)
     */
    @GetMapping("/anbieter/{anbieterId}")
    public ResponseEntity<List<AngebotMapper.AngebotResponse>> findeAngeboteFuerAnbieter(
            @PathVariable("anbieterId") String anbieterId
    ) {
        return ResponseEntity.ok(mapper.toResponseList(service.findeAngeboteFuerAnbieter(anbieterId)));
    }
}
