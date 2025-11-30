package com.foodrescue.shared.application;

import com.foodrescue.reservierungsmanagement.application.services.RescueService;
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
