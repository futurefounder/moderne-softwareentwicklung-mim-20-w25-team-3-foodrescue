package com.foodrescue.angebotsmanagement.application.commands;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ErstelleAngebotCommandTest {

  @Test
  void gettersAndSetters_roundTrip() {
    ErstelleAngebotCommand cmd = new ErstelleAngebotCommand();

    cmd.setAnbieterId("anbieter");
    cmd.setTitel("Titel");
    cmd.setBeschreibung("Beschreibung");
    cmd.setTags(Set.of("t1"));
    cmd.setVon("2025-12-14T10:00");
    cmd.setBis("2025-12-14T12:00");

    assertEquals("anbieter", cmd.getAnbieterId());
    assertEquals("Titel", cmd.getTitel());
    assertEquals("Beschreibung", cmd.getBeschreibung());
    assertEquals(Set.of("t1"), cmd.getTags());
    assertEquals("2025-12-14T10:00", cmd.getVon());
    assertEquals("2025-12-14T12:00", cmd.getBis());
  }
}
