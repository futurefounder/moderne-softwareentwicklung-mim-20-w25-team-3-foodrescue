package com.foodrescue.userverwaltung.rest;

import com.foodrescue.userverwaltung.domain.User;
import com.foodrescue.userverwaltung.valueobjects.Rolle;
import java.util.UUID;

public class UserResponse {

  private UUID id;
  private String name;
  private String email;
  private Rolle rolle;

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
