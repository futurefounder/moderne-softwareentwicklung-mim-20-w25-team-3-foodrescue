package com.foodrescue.userverwaltung.rest;

import com.foodrescue.userverwaltung.valueobjects.Rolle;

public class RegistriereUserRequest {

  private String name;
  private String email;
  private Rolle rolle;

  public RegistriereUserRequest() {
    // für JSON Deserialization
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

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setRolle(Rolle rolle) {
    this.rolle = rolle;
  }
}
