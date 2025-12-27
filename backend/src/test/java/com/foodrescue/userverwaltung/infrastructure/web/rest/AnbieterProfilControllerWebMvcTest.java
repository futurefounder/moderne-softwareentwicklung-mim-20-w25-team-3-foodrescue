package com.foodrescue.userverwaltung.infrastructure.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.foodrescue.userverwaltung.application.services.AnbieterProfilApplicationService;
import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilDetailsQuery;
import com.foodrescue.userverwaltung.domain.queries.AnbieterProfilFuerUserQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AnbieterProfilController.class)
@Import(ApiExceptionHandler.class)
class AnbieterProfilControllerWebMvcTest {

  @Autowired private MockMvc mvc;

  @MockBean private AnbieterProfilApplicationService anbieterProfilApplicationService;

  private static Adresse adresse() {
    return new Adresse("Ahornstr. 11", "12345", "Berlin", "DE");
  }

  @Test
  void post_withStructuredAddressAndGeo_returns201() throws Exception {
    UserId uid = new UserId(UUID.randomUUID());
    AnbieterProfilId pid = AnbieterProfilId.neu();
    GeoStandort geo = new GeoStandort(52.5, 13.4);

    AnbieterProfilDetailsQuery q =
        new AnbieterProfilDetailsQuery(
            pid, uid, new Geschaeftsname("Shop"), Geschaeftstyp.SUPERMARKT, adresse(), geo);

    when(anbieterProfilApplicationService.erstelleAnbieterProfil(any())).thenReturn(q);

    String body =
        "{"
            + "\"userId\":\""
            + uid.getValue()
            + "\","
            + "\"geschaeftsname\":\"Shop\","
            + "\"geschaeftstyp\":\"SUPERMARKT\","
            + "\"strasse\":\"Ahornstr. 11\","
            + "\"plz\":\"12345\","
            + "\"ort\":\"Berlin\","
            + "\"land\":\"DE\","
            + "\"breitengrad\":52.5,"
            + "\"laengengrad\":13.4"
            + "}";

    mvc.perform(post("/api/anbieter-profile").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.geschaeftsname").value("Shop"))
        .andExpect(jsonPath("$.breitengrad").value(52.5))
        .andExpect(jsonPath("$.laengengrad").value(13.4));
  }

  @Test
  void post_withLegacyAdresseParsesAndReturns201() throws Exception {
    UserId uid = new UserId(UUID.randomUUID());
    AnbieterProfilId pid = AnbieterProfilId.neu();

    AnbieterProfilDetailsQuery q =
        new AnbieterProfilDetailsQuery(
            pid, uid, new Geschaeftsname("Shop"), Geschaeftstyp.SUPERMARKT, adresse(), null);
    when(anbieterProfilApplicationService.erstelleAnbieterProfil(any())).thenReturn(q);

    String body =
        "{"
            + "\"userId\":\""
            + uid.getValue()
            + "\","
            + "\"geschaeftsname\":\"Shop\","
            + "\"geschaeftstyp\":\"SUPERMARKT\","
            + "\"adresse\":\"Ahornstr. 11, 12345 Berlin, DE\""
            + "}";

    mvc.perform(post("/api/anbieter-profile").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.strasse").value("Ahornstr. 11"))
        .andExpect(jsonPath("$.plz").value("12345"))
        .andExpect(jsonPath("$.ort").value("Berlin"))
        .andExpect(jsonPath("$.land").value("DE"))
        .andExpect(jsonPath("$.breitengrad").doesNotExist());
  }

  @Test
  void post_withOnlyOneGeoCoordinate_returns400() throws Exception {
    UserId uid = new UserId(UUID.randomUUID());

    String body =
        "{"
            + "\"userId\":\""
            + uid.getValue()
            + "\","
            + "\"geschaeftsname\":\"Shop\","
            + "\"geschaeftstyp\":\"SUPERMARKT\","
            + "\"adresse\":\"Ahornstr. 11, 12345 Berlin, DE\","
            + "\"breitengrad\":52.5"
            + "}";

    mvc.perform(post("/api/anbieter-profile").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void post_withInvalidLegacyAdresse_returns400() throws Exception {
    UserId uid = new UserId(UUID.randomUUID());

    String body =
        "{"
            + "\"userId\":\""
            + uid.getValue()
            + "\","
            + "\"geschaeftsname\":\"Shop\","
            + "\"geschaeftstyp\":\"SUPERMARKT\","
            + "\"adresse\":\"INVALID\""
            + "}";

    mvc.perform(post("/api/anbieter-profile").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void get_userProfil_returns200() throws Exception {
    UserId uid = new UserId(UUID.randomUUID());
    AnbieterProfil profil =
        AnbieterProfil.erstellenFuerAnbieter(
            AnbieterProfilId.neu(),
            uid,
            Rolle.ANBIETER,
            new Geschaeftsname("Shop"),
            Geschaeftstyp.SUPERMARKT,
            adresse(),
            null);

    User user =
        User.registrieren(
            uid, new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ANBIETER);

    when(anbieterProfilApplicationService.findeAnbieterProfilFuerUser(any(UserId.class)))
        .thenReturn(new AnbieterProfilFuerUserQuery(user, profil));

    mvc.perform(get("/api/anbieter-profile/user/{userId}", uid.getValue().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value(uid.getValue().toString()))
        .andExpect(jsonPath("$.geschaeftsname").value("Shop"));
  }
}
