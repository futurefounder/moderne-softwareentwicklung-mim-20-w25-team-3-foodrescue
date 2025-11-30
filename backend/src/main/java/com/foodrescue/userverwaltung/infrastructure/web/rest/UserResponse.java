package com.foodrescue.userverwaltung.infrastructure.web.rest;

import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import java.util.UUID;

public class UserResponse {

  private final UUID id;
  private final String name;
  private final String email;
  private final Rolle rolle;

  public UserResponse(UUID id, String name, String email, Rolle rolle) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.rolle = rolle;
  }

  public static UserResponse fromDomain(User user) {
    return new UserResponse(
        user.getId().getValue(),
        user.getName().getValue(),
        user.getEmail().getValue(),
        user.getRolle());
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public Rolle getRolle() {
    return rolle;
  }
}
