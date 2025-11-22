package com.foodrescue.userverwaltung.rest;

import com.foodrescue.userverwaltung.application.UserApplicationService;
import com.foodrescue.userverwaltung.commands.RegistriereUserCommand;
import com.foodrescue.userverwaltung.domain.User;
import com.foodrescue.userverwaltung.valueobjects.EmailAdresse;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserApplicationService userApplicationService;

  public UserController(UserApplicationService userApplicationService) {
    this.userApplicationService = userApplicationService;
  }

  @PostMapping
  public ResponseEntity<UserResponse> registriereUser(@RequestBody RegistriereUserRequest request) {
    // Validate request fields in order: name, email (including format), then role
    if (request.getName() == null || request.getName().trim().isEmpty()) {
      throw new IllegalArgumentException("Bitte geben Sie einen Namen ein");
    }
    if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
      throw new IllegalArgumentException("Bitte geben Sie eine E-Mail-Adresse ein");
    }
    // Validate email format early by creating EmailAdresse object
    new EmailAdresse(request.getEmail());

    if (request.getRolle() == null) {
      throw new IllegalArgumentException("Bitte wählen Sie eine Rolle aus");
    }

    RegistriereUserCommand command =
        new RegistriereUserCommand(request.getName(), request.getEmail(), request.getRolle());

    UUID id = userApplicationService.registriereUser(command).getValue();

    User user = userApplicationService.holeUserDetails(id);

    return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(user));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> holeUser(@PathVariable("id") UUID id) {
    User user = userApplicationService.holeUserDetails(id);
    return ResponseEntity.ok(UserResponse.fromDomain(user));
  }
}
