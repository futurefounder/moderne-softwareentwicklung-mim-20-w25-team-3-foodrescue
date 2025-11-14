package com.foodrescue.anbieterverwaltung.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodrescue.anbieterverwaltung.application.AnbieterProfilApplicationService;
import com.foodrescue.anbieterverwaltung.domain.AnbieterProfil;
import com.foodrescue.anbieterverwaltung.valueobjects.Adresse;
import com.foodrescue.anbieterverwaltung.valueobjects.AnbieterProfilId;
import com.foodrescue.anbieterverwaltung.valueobjects.GeoStandort;
import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftsname;
import com.foodrescue.anbieterverwaltung.valueobjects.Geschaeftstyp;
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
public class AnbieterProfilControllerTest {

  @Mock private AnbieterProfilApplicationService applicationService;

  @InjectMocks private AnbieterProfilController controller;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  private UUID profilUuid;
  private UUID userUuid;
  private AnbieterProfil profil;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    objectMapper = new ObjectMapper();

    profilUuid = UUID.randomUUID();
    userUuid = UUID.randomUUID();

    profil =
        AnbieterProfil.erstellenFuerAnbieter(
            new AnbieterProfilId(profilUuid),
            new UserId(userUuid),
            Rolle.ANBIETER,
            new Geschaeftsname("Baeckerei Schmidt"),
            Geschaeftstyp.BAECKEREI,
            new Adresse("Strasse 1", "12345", "Stadt", "DE"),
            new GeoStandort(50.0, 8.0));
  }

  @Test
  void erstelleProfilErzeugtProfilUndGibt201Zurueck() throws Exception {
    ErstelleAnbieterProfilRequest request = new ErstelleAnbieterProfilRequest();
    request.setUserId(userUuid);
    request.setUserRolle(Rolle.ANBIETER);
    request.setGeschaeftsname("Baeckerei Schmidt");
    request.setGeschaeftstyp(Geschaeftstyp.BAECKEREI);
    request.setStrasse("Strasse 1");
    request.setPlz("12345");
    request.setOrt("Stadt");
    request.setLand("DE");
    request.setBreitengrad(50.0);
    request.setLaengengrad(8.0);

    when(applicationService.erstelleAnbieterProfil(any()))
        .thenReturn(new AnbieterProfilId(profilUuid));
    when(applicationService.holeProfil(profilUuid)).thenReturn(profil);

    mockMvc
        .perform(
            post("/api/anbieterprofile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(profilUuid.toString()))
        .andExpect(jsonPath("$.userId").value(userUuid.toString()))
        .andExpect(jsonPath("$.geschaeftsname").value("Baeckerei Schmidt"))
        .andExpect(jsonPath("$.geschaeftstyp").value("BAECKEREI"))
        .andExpect(jsonPath("$.strasse").value("Strasse 1"))
        .andExpect(jsonPath("$.plz").value("12345"))
        .andExpect(jsonPath("$.ort").value("Stadt"))
        .andExpect(jsonPath("$.land").value("DE"))
        .andExpect(jsonPath("$.breitengrad").value(50.0))
        .andExpect(jsonPath("$.laengengrad").value(8.0));
  }

  @Test
  void holeProfilGibtProfilMit200Zurueck() throws Exception {
    when(applicationService.holeProfil(profilUuid)).thenReturn(profil);

    mockMvc
        .perform(get("/api/anbieterprofile/{id}", profilUuid.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(profilUuid.toString()))
        .andExpect(jsonPath("$.userId").value(userUuid.toString()))
        .andExpect(jsonPath("$.geschaeftsname").value("Baeckerei Schmidt"));
  }

  @Test
  void holeProfilFuerUserGibtProfilMit200Zurueck() throws Exception {
    when(applicationService.holeProfilFuerUser(userUuid)).thenReturn(profil);

    mockMvc
        .perform(get("/api/anbieterprofile/user/{userId}", userUuid.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(profilUuid.toString()))
        .andExpect(jsonPath("$.userId").value(userUuid.toString()))
        .andExpect(jsonPath("$.geschaeftsname").value("Baeckerei Schmidt"));
  }
}
