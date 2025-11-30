package com.foodrescue.reservierungsmanagement.application.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.foodrescue.reservierungsmanagement.application.services.RescueService;
import org.junit.jupiter.api.Test;

public class RescueServiceTest {

  private final RescueService service = new RescueService();

  @Test
  void filtersNonPerishables() {
    var input = List.of("frische Milch", "Konserven", "Pasta", "frische Beeren");
    var result = service.filterNonPerishables(input);
    assertThat(result).containsExactlyInAnyOrder("Konserven", "Pasta");
  }

  @Test
  void healthReturnsOk() {
    // deckt die Methode RescueService.health() ab
    assertThat(service.health()).isEqualTo("OK");
  }
}
