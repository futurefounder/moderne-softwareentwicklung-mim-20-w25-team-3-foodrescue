package com.foodrescue.userverwaltung.infrastructure.web.rest;

import com.foodrescue.userverwaltung.application.commands.RegistriereUserCommand;
import com.foodrescue.userverwaltung.application.services.UserApplicationService;
import com.foodrescue.userverwaltung.domain.queries.UserDetailsQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
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

  @PostMapping("/registrierung")
  public ResponseEntity<UserResponse> registriereUser(@RequestBody RegistriereUserRequest request) {

    RegistriereUserCommand command =
        new RegistriereUserCommand(
            new Name(request.getName()),
            new EmailAdresse(request.getEmail()),
            Rolle.valueOf(request.getRolle()));

    UserDetailsQuery result = userApplicationService.registriereUser(command);

    UserResponse response =
        new UserResponse(
            result.getUserId().getValue(),
            result.getName().toString(),
            result.getEmail().toString(),
            result.getRolle());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
