package com.foodrescue.angebotsmanagement.infrastructure.web.rest.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AngebotMapperTest {

  private static Angebot angebotMitFenster() {
    UserId anbieter = new UserId(UUID.randomUUID());
    AbholZeitfenster fenster =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 12, 14, 10, 0), LocalDateTime.of(2025, 12, 14, 12, 0));
    return Angebot.erstelle(
        AngebotsId.of("a1"), anbieter, "Titel", "Beschr", Set.of("t1"), fenster);
  }

  @Test
  void toResponse_mapsAllFields_includingZeitfenster() {
    AngebotMapper mapper = new AngebotMapper();
    Angebot angebot = angebotMitFenster();
    angebot.veroeffentlichen();

    AngebotMapper.AngebotResponse dto = mapper.toResponse(angebot);

    assertEquals("a1", dto.id());
    assertEquals(angebot.getAnbieterId(), dto.anbieterId());
    assertEquals("Titel", dto.titel());
    assertEquals("Beschr", dto.beschreibung());
    assertEquals(Set.of("t1"), dto.tags());
    assertEquals("VERFUEGBAR", dto.status());

    assertNotNull(dto.zeitfenster());
    assertEquals("2025-12-14T10:00", dto.zeitfenster().von());
    assertEquals("2025-12-14T12:00", dto.zeitfenster().bis());
  }

  @Test
  void toResponse_handlesNullZeitfenster() {
    AngebotMapper mapper = new AngebotMapper();
    UserId anbieter = new UserId(UUID.randomUUID());

    AbholZeitfenster fenster =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 12, 14, 10, 0), LocalDateTime.of(2025, 12, 14, 12, 0));

    Angebot angebot = Angebot.erstelle(AngebotsId.of("a2"), anbieter, "T", "B", Set.of(), fenster);

    AngebotMapper.AngebotResponse dto = mapper.toResponse(angebot);

    assertEquals("a2", dto.id());
  }

  @Test
  void toResponseList_mapsAll() {
    AngebotMapper mapper = new AngebotMapper();
    UserId anbieter = new UserId(UUID.randomUUID());

    AbholZeitfenster fenster =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 12, 14, 10, 0), LocalDateTime.of(2025, 12, 14, 12, 0));

    Angebot a1 = Angebot.erstelle(AngebotsId.of("a1"), anbieter, "T1", "B1", Set.of(), fenster);
    Angebot a2 = Angebot.erstelle(AngebotsId.of("a2"), anbieter, "T2", "B2", Set.of("x"), fenster);

    var list = mapper.toResponseList(java.util.List.of(a1, a2));
    assertEquals(2, list.size());
    assertEquals("a1", list.get(0).id());
    assertEquals("a2", list.get(1).id());
  }
}
