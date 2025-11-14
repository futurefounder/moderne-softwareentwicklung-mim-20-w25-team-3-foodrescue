package com.foodrescue.userverwaltung.rest;

import com.foodrescue.userverwaltung.application.UserApplicationService;
import com.foodrescue.userverwaltung.commands.RegistriereUserCommand;
import com.foodrescue.userverwaltung.domain.User;
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
