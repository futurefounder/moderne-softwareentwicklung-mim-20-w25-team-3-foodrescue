package com.foodrescue;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.foodrescue.userverwaltung.infrastructure.repositories.AnbieterProfilRepository;
import com.foodrescue.userverwaltung.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class FoodRescueApplicationTest {

  @MockBean AnbieterProfilRepository anbieterProfilRepository;

  @MockBean UserRepository userRepository;

  @Test
  void mainStartsApplicationWithoutErrors() {
    String[] args = {};
    // Deckt die main-Methode inkl. SpringApplication.run(...) ab
    assertDoesNotThrow(() -> FoodRescueApplication.main(args));
  }
}
