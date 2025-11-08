package com.foodrescue;

import static org.assertj.core.api.Assertions.assertThat;

import com.foodrescue.application.services.RescueService;
import java.util.List;
import org.junit.jupiter.api.Test;

class RescueServiceTest {
  private final RescueService service = new RescueService();

  @Test
  void filtersNonPerishables() {
    var input = List.of("frische Milch", "Konserven", "Pasta", "frische Beeren");
    var result = service.filterNonPerishables(input);
    assertThat(result).containsExactlyInAnyOrder("Konserven", "Pasta");
  }
}
