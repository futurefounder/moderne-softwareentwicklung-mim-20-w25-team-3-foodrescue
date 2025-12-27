package com.foodrescue.reservierungsmanagement.application.commands;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReserviereAngebotCommandTest {

  @Test
  void getters_returnConstructorArguments() {
    AngebotsId angebotId = new AngebotsId("a1");
    UserId abholerId = new UserId(UUID.randomUUID());

    ReserviereAngebotCommand cmd = new ReserviereAngebotCommand(angebotId, abholerId);

    assertSame(angebotId, cmd.getAngebotId());
    assertSame(abholerId, cmd.getAbholerId());
  }
}
