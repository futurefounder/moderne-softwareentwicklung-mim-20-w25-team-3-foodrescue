package com.foodrescue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FoodRescueApplicationTest {

    @Test
    void mainStartsApplicationWithoutErrors() {
        String[] args = {};
        // Deckt die main-Methode inkl. SpringApplication.run(...) ab
        assertDoesNotThrow(() -> FoodRescueApplication.main(args));
    }
}

