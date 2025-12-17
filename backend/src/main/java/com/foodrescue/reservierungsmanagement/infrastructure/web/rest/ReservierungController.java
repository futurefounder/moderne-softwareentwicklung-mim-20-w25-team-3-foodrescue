package com.foodrescue.reservierungsmanagement.infrastructure.web.rest;

import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.reservierungsmanagement.application.commands.ReserviereAngebotCommand;
import com.foodrescue.reservierungsmanagement.application.services.ReservierungsApplicationService;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reservierungen", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservierungController {

    private final ReservierungsApplicationService service;

    public ReservierungController(ReservierungsApplicationService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateReservierungResponse> reserviere(@RequestBody ReserviereAngebotRequest request) {
        if (request == null || request.getAngebotId() == null || request.getAbholerId() == null) {
            return ResponseEntity.badRequest().build();
        }

        var cmd = new ReserviereAngebotCommand(
                new AngebotsId(request.getAngebotId()),
                new UserId(request.getAbholerId())
        );

        ReservierungsId id = service.reserviereAngebot(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateReservierungResponse(id.value()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GeplanteAbholungResponse>> pickups(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(service.findeGeplanteAbholungenFuerUser(userId));
    }

    public record CreateReservierungResponse(String id) {}

    public record GeplanteAbholungResponse(
            String reservierungId,
            String angebotId,
            String angebotTitel,
            String angebotBeschreibung,
            String status,
            String abholcode,
            String zeitfensterVon,
            String zeitfensterBis
    ) {}
}
