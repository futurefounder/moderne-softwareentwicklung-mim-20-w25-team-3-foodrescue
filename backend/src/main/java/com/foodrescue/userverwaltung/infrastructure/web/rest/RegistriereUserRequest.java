package com.foodrescue.userverwaltung.infrastructure.web.rest;

public class RegistriereUserRequest {

  private String name;
  private String email;
  private String rolle; // "ABHOLER" | "ANBIETER"

  public RegistriereUserRequest() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRolle() {
    return rolle;
  }

  public void setRolle(String rolle) {
    this.rolle = rolle;
  }
}
