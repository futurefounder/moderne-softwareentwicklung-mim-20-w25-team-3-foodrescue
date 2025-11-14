package com.foodrescue.userverwaltung.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodrescue.userverwaltung.application.UserApplicationService;
import com.foodrescue.userverwaltung.domain.User;
import com.foodrescue.userverwaltung.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.valueobjects.Name;
import com.foodrescue.userverwaltung.valueobjects.Rolle;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

  @Mock private UserApplicationService userApplicationService;

  @InjectMocks private UserController userController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void registriereUserErzeugtUserUndGibt201Zurueck() throws Exception {
    UUID uuid = UUID.randomUUID();
    UserId userId = new UserId(uuid);
    User user =
        new User(userId, new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ABHOLER);

    RegistriereUserRequest request = new RegistriereUserRequest();
    request.setName("Max");
    request.setEmail("max@example.com");
    request.setRolle(Rolle.ABHOLER);

    when(userApplicationService.registriereUser(any())).thenReturn(userId);
    when(userApplicationService.holeUserDetails(uuid)).thenReturn(user);

    mockMvc
        .perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(uuid.toString()))
        .andExpect(jsonPath("$.name").value("Max"))
        .andExpect(jsonPath("$.email").value("max@example.com"))
        .andExpect(jsonPath("$.rolle").value("ABHOLER"));
  }

  @Test
  void holeUserGibtUserMit200Zurueck() throws Exception {
    UUID uuid = UUID.randomUUID();
    User user =
        new User(
            new UserId(uuid), new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ABHOLER);

    when(userApplicationService.holeUserDetails(uuid)).thenReturn(user);

    mockMvc
        .perform(get("/api/users/{id}", uuid.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(uuid.toString()))
        .andExpect(jsonPath("$.name").value("Max"))
        .andExpect(jsonPath("$.email").value("max@example.com"))
        .andExpect(jsonPath("$.rolle").value("ABHOLER"));
  }
}
