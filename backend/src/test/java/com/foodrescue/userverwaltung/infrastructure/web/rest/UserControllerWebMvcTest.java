package com.foodrescue.userverwaltung.infrastructure.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.foodrescue.userverwaltung.application.services.UserApplicationService;
import com.foodrescue.userverwaltung.domain.queries.UserDetailsQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserController.class)
@Import(ApiExceptionHandler.class)
class UserControllerWebMvcTest {

  @Autowired private MockMvc mvc;

  @MockBean private UserApplicationService userApplicationService;

  @Test
  void registriereUser_returns201AndBody() throws Exception {
    UserDetailsQuery q =
        new UserDetailsQuery(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);

    when(userApplicationService.registriereUser(any())).thenReturn(q);

    String body =
        "{"
            + "\"name\":\"Max\","
            + "\"email\":\"max@example.com\","
            + "\"rolle\":\"ABHOLER\""
            + "}";

    mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Max"))
        .andExpect(jsonPath("$.email").value("max@example.com"))
        .andExpect(jsonPath("$.rolle").value("ABHOLER"));
  }

  @Test
  void findeUserByEmail_returns200() throws Exception {
    UserDetailsQuery q =
        new UserDetailsQuery(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);

    when(userApplicationService.findeUserByEmail(any(EmailAdresse.class))).thenReturn(q);

    mvc.perform(get("/api/users/by-email").param("email", "max@example.com"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email").value("max@example.com"));
  }

  @Test
  void findeUserByEmail_invalidEmail_returns400ViaExceptionHandler() throws Exception {
    // EmailAdresse ctor should throw IllegalArgumentException; ApiExceptionHandler maps to 400
    mvc.perform(get("/api/users/by-email").param("email", "not-an-email"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists());
  }
}
