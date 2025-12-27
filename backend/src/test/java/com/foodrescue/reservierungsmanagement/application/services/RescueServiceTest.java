package com.foodrescue.reservierungsmanagement.application.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class RescueServiceTest {

  @Test
  void health_returnsOK() {
    RescueService service = new RescueService();
    assertEquals("OK", service.health());
  }

  @Test
  void filterNonPerishables_filtersItemsContainingFrisch_caseInsensitive() {
    RescueService service = new RescueService();

    List<String> result =
        service.filterNonPerishables(
            List.of("Brot", "Frischmilch", "frisch Käse", "Konserve", "TROCKENWARE"));

    assertEquals(List.of("Brot", "Konserve", "TROCKENWARE"), result);
  }

  @Test
  void filterNonPerishables_emptyList_returnsEmpty() {
    RescueService service = new RescueService();
    assertTrue(service.filterNonPerishables(List.of()).isEmpty());
  }
}
