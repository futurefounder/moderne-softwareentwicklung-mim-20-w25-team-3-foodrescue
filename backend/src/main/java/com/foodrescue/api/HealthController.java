package com.example.foodrescue.api;

import com.example.foodrescue.core.RescueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HealthController {
    private final RescueService service;

    public HealthController(RescueService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok(service.health());
    }
}
