package com.foodrescue.angebotsmanagement.application.services;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AngebotDomainServiceTest {

  @Test
  void erstelleAngebot_delegatesToAggregateFactory() {
    AngebotDomainService svc = new AngebotDomainService();

    AngebotsId id = AngebotsId.of("a1");
    UserId anbieter = new UserId(UUID.randomUUID());
    AbholZeitfenster fenster =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 1, 1, 10, 0), LocalDateTime.of(2025, 1, 1, 11, 0));

    Angebot angebot =
        svc.erstelleAngebot(id, anbieter, "Titel", "Beschreibung", Set.of("t1"), fenster);

    assertNotNull(angebot);
    assertEquals("a1", angebot.getId());
    assertEquals("Titel", angebot.getTitel());
    assertEquals("Beschreibung", angebot.getBeschreibung());
    assertEquals(Set.of("t1"), angebot.getTags());
    assertEquals("ENTWURF", angebot.getStatus().name());
    assertEquals(fenster, angebot.getZeitfenster());
  }
}
