package com.foodrescue.angebotsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.shared.exception.DomainException;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class AngebotEdgeCasesTest {

  @Test
  void veroeffentlichen_doppelt_nicht_erlaubt() {
    var f = AbholZeitfenster.of(LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
    var a = Angebot.neu("a1", "anb", "Titel", "", Set.of(), f);
    a.veroeffentlichen();
    assertThrows(DomainException.class, a::veroeffentlichen);
  }

  @Test
  void zeitfenster_mussChronologischSein() {
    assertThrows(
        DomainException.class,
        () ->
            AbholZeitfenster.of(
                LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(1)));
  }
}
