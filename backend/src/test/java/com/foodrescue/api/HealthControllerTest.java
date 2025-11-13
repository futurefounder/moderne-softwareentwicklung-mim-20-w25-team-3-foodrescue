package com.foodrescue.api;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.foodrescue.application.services.RescueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HealthController.class)
public class HealthControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private RescueService service;

  @Test
  void healthEndpointReturnsOk() throws Exception {
    given(service.health()).willReturn("OK");
    mvc.perform(get("/api/health")).andExpect(status().isOk()).andExpect(content().string("OK"));
  }
}
