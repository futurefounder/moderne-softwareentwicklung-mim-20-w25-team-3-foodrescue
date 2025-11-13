package com.foodrescue;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class FoodRescueApplicationTest {

  @Test
  void mainStartsApplicationWithoutErrors() {
    String[] args = {};
    // Deckt die main-Methode inkl. SpringApplication.run(...) ab
    assertDoesNotThrow(() -> FoodRescueApplication.main(args));
  }
}
