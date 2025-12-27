package com.foodrescue.angebotsmanagement.infrastructure.web.rest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.application.services.AngebotApplicationService;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.web.rest.mapper.AngebotMapper;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AngebotController.class)
@Import(AngebotMapper.class)
class AngebotControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @MockBean AngebotApplicationService service;

  @Test
  void post_apiAngebote_createsAndReturns201WithId() throws Exception {
    when(service.erstelleAngebot(any())).thenReturn(AngebotsId.of("a1"));

    String json =
        """
      {
        "anbieterId": "%s",
        "titel": "Titel",
        "beschreibung": "Beschreibung",
        "tags": ["t1","t2"],
        "zeitfenster": { "von": "2025-12-14T10:00", "bis": "2025-12-14T12:00" }
      }
      """
            .formatted(UUID.randomUUID());

    mockMvc
        .perform(
            post("/api/angebote")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("a1"));

    verify(service).erstelleAngebot(any());
  }

  @Test
  void post_veroeffentlichen_returns200_andDelegates() throws Exception {
    mockMvc.perform(post("/api/angebote/{id}/veroeffentlichen", "a1")).andExpect(status().isOk());

    verify(service).veroeffentlicheAngebot(any());
  }

  @Test
  void get_verfuegbar_returnsList() throws Exception {
    Angebot angebot =
        Angebot.erstelle(
            AngebotsId.of("a1"),
            new UserId(UUID.randomUUID()),
            "Titel",
            "Beschreibung",
            Set.of("t1"),
            new AbholZeitfenster(
                LocalDateTime.of(2025, 12, 14, 10, 0), LocalDateTime.of(2025, 12, 14, 12, 0)));
    angebot.veroeffentlichen();

    when(service.findeVerfuegbareAngebote()).thenReturn(List.of(angebot));

    mockMvc
        .perform(get("/api/angebote/verfuegbar").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value("a1"))
        .andExpect(jsonPath("$[0].status").value("VERFUEGBAR"))
        .andExpect(jsonPath("$[0].zeitfenster.von").value("2025-12-14T10:00"));

    verify(service).findeVerfuegbareAngebote();
  }

  @Test
  void get_details_returnsDto() throws Exception {

    AbholZeitfenster fenster =
        new AbholZeitfenster(
            LocalDateTime.of(2025, 12, 14, 10, 0), LocalDateTime.of(2025, 12, 14, 12, 0));

    Angebot angebot =
        Angebot.erstelle(
            AngebotsId.of("a1"),
            new UserId(UUID.randomUUID()),
            "Titel",
            "Beschreibung",
            Set.of(),
            fenster);

    when(service.findeAngebotMitId(any())).thenReturn(angebot);

    mockMvc
        .perform(get("/api/angebote/{id}", "a1").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("a1"));

    verify(service).findeAngebotMitId(any());
  }

  @Test
  void post_create_malformedJson_returns400() throws Exception {
    String json = "{ \"anbieterId\": \"x\", ";

    mockMvc
        .perform(post("/api/angebote").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).erstelleAngebot(any());
  }
}
