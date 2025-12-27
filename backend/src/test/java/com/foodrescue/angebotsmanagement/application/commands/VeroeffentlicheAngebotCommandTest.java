package com.foodrescue.angebotsmanagement.application.commands;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import org.junit.jupiter.api.Test;

class VeroeffentlicheAngebotCommandTest {

  @Test
  void getAngebotId_returnsConstructorArgument() {
    AngebotsId id = AngebotsId.of("a1");
    VeroeffentlicheAngebotCommand cmd = new VeroeffentlicheAngebotCommand(id);

    assertSame(id, cmd.getAngebotId());
  }
}
