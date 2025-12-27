package com.foodrescue.abholungsmanagement.infrastructure.web.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.foodrescue.abholungsmanagement.application.commands.BestaetigeAbholungCommand;
import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestJacksonWebMvcConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();

    // Mixin: sagt Jackson, welchen Constructor es verwenden soll und welche JSON-Felder gemappt
    // werden.
    mapper.addMixIn(BestaetigeAbholungCommand.class, BestaetigeAbholungCommandMixin.class);

    SimpleModule voModule = new SimpleModule();
    voModule.addDeserializer(
        Abholcode.class, new ReflectiveValueObjectDeserializer<>(Abholcode.class));
    voModule.addDeserializer(
        ReservierungsId.class, new ReflectiveValueObjectDeserializer<>(ReservierungsId.class));

    mapper.registerModule(voModule);
    return mapper;
  }

  // Mixin für BestaetigeAbholungCommand (ohne Produktionscode zu ändern)
  abstract static class BestaetigeAbholungCommandMixin {
    @JsonCreator
    BestaetigeAbholungCommandMixin(
        @JsonProperty(value = "reservierungsId", required = true) ReservierungsId reservierungsId,
        @JsonProperty(value = "code", required = true) Abholcode code) {}
  }

  /**
   * Generischer Deserializer für Value Objects: - bevorzugt static of(String) - fallback:
   * Constructor(String)
   *
   * <p>Wirft bei Problemen eine JsonMappingException => MockMvc liefert 400 Bad Request.
   */
  static class ReflectiveValueObjectDeserializer<T> extends JsonDeserializer<T> {
    private final Class<T> type;

    ReflectiveValueObjectDeserializer(Class<T> type) {
      this.type = type;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      String raw = p.getValueAsString();
      if (raw == null) {
        return null;
      }

      try {
        // static of(String)
        Method of = type.getMethod("of", String.class);
        @SuppressWarnings("unchecked")
        T v = (T) of.invoke(null, raw);
        return v;
      } catch (NoSuchMethodException ignored) {
        // fallback below
      } catch (Exception e) {
        throw JsonMappingException.from(
            p,
            "Failed to create " + type.getSimpleName() + " via of(String): " + e.getMessage(),
            e);
      }

      try {
        // new T(String)
        Constructor<T> c = type.getDeclaredConstructor(String.class);
        c.setAccessible(true);
        return c.newInstance(raw);
      } catch (Exception e) {
        throw JsonMappingException.from(
            p,
            "Failed to create " + type.getSimpleName() + " via ctor(String): " + e.getMessage(),
            e);
      }
    }
  }
}
