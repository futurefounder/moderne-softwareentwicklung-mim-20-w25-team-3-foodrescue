package com.foodrescue;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.foodrescue.anbieterverwaltung.repositories.AnbieterProfilRepository;
import com.foodrescue.userverwaltung.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class FoodRescueApplicationTest {

  @MockBean AnbieterProfilRepository anbieterProfilRepository;

  @MockBean UserRepository userRepository;

  @Test
  void mainStartsApplicationWithoutErrors() {
    String[] args = {};
    // Deckt die main-Methode inkl. SpringApplication.run(...) ab
    assertDoesNotThrow(() -> FoodRescueApplication.main(args));
  }
}
