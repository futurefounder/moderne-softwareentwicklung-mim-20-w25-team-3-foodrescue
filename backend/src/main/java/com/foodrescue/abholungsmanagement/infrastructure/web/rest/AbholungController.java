package com.foodrescue.abholungsmanagement.infrastructure.web.rest;

import com.foodrescue.abholungsmanagement.application.commands.BestaetigeAbholungCommand;
import com.foodrescue.abholungsmanagement.application.services.AbholungApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/abholungen")
public class AbholungController {

    private final AbholungApplicationService service;

    public AbholungController(AbholungApplicationService service) {
        this.service = service;
    }

    @PostMapping("/bestaetigen")
    public ResponseEntity<Void> bestaetigeAbholung(@RequestBody BestaetigeAbholungCommand command) {
        service.bestaetigeAbholung(command);
        return ResponseEntity.ok().build();
    }
}
