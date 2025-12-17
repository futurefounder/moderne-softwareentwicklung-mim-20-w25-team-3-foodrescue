package com.foodrescue.reservierungsmanagement.application.commands;

import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;

public class ReserviereAngebotCommand {
  private final AngebotsId angebotId;
  private final UserId abholerId;

  public ReserviereAngebotCommand(AngebotsId angebotId, UserId abholerId) {
    this.angebotId = angebotId;
    this.abholerId = abholerId;
  }

  public AngebotsId getAngebotId() {
    return angebotId;
  }

  public UserId getAbholerId() {
    return abholerId;
  }
}
