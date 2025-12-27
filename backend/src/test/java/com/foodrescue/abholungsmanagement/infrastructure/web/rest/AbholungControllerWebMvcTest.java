package com.foodrescue.abholungsmanagement.infrastructure.web.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.foodrescue.abholungsmanagement.application.commands.BestaetigeAbholungCommand;
import com.foodrescue.abholungsmanagement.application.services.AbholungApplicationService;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AbholungController.class)
@Import(TestJacksonWebMvcConfig.class)
class AbholungControllerWebMvcTest {

  @Autowired MockMvc mockMvc;

  @org.springframework.boot.test.mock.mockito.MockBean AbholungApplicationService service;

  @Test
  void post_bestaetigen_happyPath_returns200_andDelegatesToService() throws Exception {
    String json =
        """
      {
        "reservierungsId": "res-1",
        "code": "AB12"
      }
      """;

    mockMvc
        .perform(
            post("/api/abholungen/bestaetigen")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string(""));

    ArgumentCaptor<BestaetigeAbholungCommand> captor =
        ArgumentCaptor.forClass(BestaetigeAbholungCommand.class);
    verify(service).bestaetigeAbholung(captor.capture());

    BestaetigeAbholungCommand cmd = captor.getValue();
    assertNotNull(cmd);

    // ReservierungsId: je nach Implementierung (value(), toString(), equals) prüfen wir robust.
    assertNotNull(cmd.getReservierungsId());
    assertEquals("res-1", extractValue(cmd.getReservierungsId()));

    Abholcode code = cmd.getCode();
    assertNotNull(code);
    assertEquals("AB12", code.toString());
  }

  @Test
  void post_bestaetigen_missingField_code_returns400() throws Exception {
    String json =
        """
      {
        "reservierungsId": "res-1"
      }
      """;

    mockMvc
        .perform(
            post("/api/abholungen/bestaetigen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).bestaetigeAbholung(any());
  }

  @Test
  void post_bestaetigen_missingField_reservierungsId_returns400() throws Exception {
    String json =
        """
      {
        "code": "AB12"
      }
      """;

    mockMvc
        .perform(
            post("/api/abholungen/bestaetigen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).bestaetigeAbholung(any());
  }

  @Test
  void post_bestaetigen_invalidAbholcode_returns400() throws Exception {
    // Abholcode verlangt [A-Z0-9]{4,8} => lowercase/zu kurz => Deserialisierung schlägt fehl
    String json =
        """
      {
        "reservierungsId": "res-1",
        "code": "ab1"
      }
      """;

    mockMvc
        .perform(
            post("/api/abholungen/bestaetigen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).bestaetigeAbholung(any());
  }

  @Test
  void post_bestaetigen_malformedJson_returns400() throws Exception {
    String json =
        """
      { "reservierungsId": "res-1", "code": "AB12"
      """;

    mockMvc
        .perform(
            post("/api/abholungen/bestaetigen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
        .andExpect(status().isBadRequest());

    verify(service, never()).bestaetigeAbholung(any());
  }

  private static String extractValue(Object valueObject) {
    // Robust gegen unterschiedliche VO-Implementierungen (value(), getValue(), toString()).
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
