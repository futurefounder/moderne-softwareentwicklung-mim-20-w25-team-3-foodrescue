package com.foodrescue.reservierungsmanagement.infrastructure.web.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.foodrescue.reservierungsmanagement.application.commands.ReserviereAngebotCommand;
import com.foodrescue.reservierungsmanagement.application.services.ReservierungsApplicationService;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ReservierungController.class)
class ReservierungControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @MockBean ReservierungsApplicationService service;

  @Test
  void post_reserviere_happyPath_returns201_andDelegatesToService() throws Exception {
    when(service.reserviereAngebot(any())).thenReturn(ReservierungsId.of("r1"));

    UUID uid = UUID.randomUUID();
    String json =
        """
      { "angebotId": "a1", "abholerId": "%s" }
      """
            .formatted(uid);

    mockMvc
        .perform(
            post("/api/reservierungen")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("r1"));

    ArgumentCaptor<ReserviereAngebotCommand> captor =
        ArgumentCaptor.forClass(ReserviereAngebotCommand.class);
    verify(service).reserviereAngebot(captor.capture());

    ReserviereAngebotCommand cmd = captor.getValue();
    assertNotNull(cmd);
    assertEquals("a1", extractValue(cmd.getAngebotId()));
    assertEquals(uid.toString(), extractValue(cmd.getAbholerId()));
  }

  @Test
  void post_reserviere_missingAngebotId_returns400_andDoesNotCallService() throws Exception {
    UUID uid = UUID.randomUUID();
    String json =
        """
      { "abholerId": "%s" }
      """
            .formatted(uid);

    mockMvc
        .perform(post("/api/reservierungen").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).reserviereAngebot(any());
  }

  @Test
  void post_reserviere_missingAbholerId_returns400_andDoesNotCallService() throws Exception {
    String json =
        """
      { "angebotId": "a1" }
      """;

    mockMvc
        .perform(post("/api/reservierungen").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).reserviereAngebot(any());
  }

  @Test
  void post_reserviere_malformedJson_returns400() throws Exception {
    String json =
        """
      { "angebotId": "a1", "abholerId":
      """;

    mockMvc
        .perform(post("/api/reservierungen").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).reserviereAngebot(any());
  }

  @Test
  void get_pickups_returnsListFromService() throws Exception {
    when(service.findeGeplanteAbholungenFuerUser("u1"))
        .thenReturn(
            List.of(
                new ReservierungController.GeplanteAbholungResponse(
                    "r1",
                    "a1",
                    "Titel",
                    "Beschr",
                    "AKTIV",
                    "AB12",
                    "2025-01-01T10:00:00",
                    "2025-01-01T11:00:00")));

    mockMvc
        .perform(get("/api/reservierungen/user/u1"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].reservierungId").value("r1"))
        .andExpect(jsonPath("$[0].angebotId").value("a1"))
        .andExpect(jsonPath("$[0].angebotTitel").value("Titel"))
        .andExpect(jsonPath("$[0].abholcode").value("AB12"));

    verify(service).findeGeplanteAbholungenFuerUser("u1");
  }

  private static String extractValue(Object valueObject) {
    if (valueObject == null) return null;

    try {
      var m = valueObject.getClass().getMethod("value");
      Object v = m.invoke(valueObject);
      return v == null ? null : v.toString();
    } catch (Exception ignored) {
    }

    try {
      var m = valueObject.getClass().getMethod("getValue");
      Object v = m.invoke(valueObject);
      return v == null ? null : v.toString();
    } catch (Exception ignored) {
    }

    return valueObject.toString();
  }
}
