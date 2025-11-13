package com.foodrescue.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DomainErrorTest {

    @Test
    void eachErrorHasANonBlankDefaultMessage() {
        for (DomainError error : DomainError.values()) {
            assertNotNull(error.defaultMessage(), error.name());
            assertFalse(error.defaultMessage().isBlank(), error.name());
        }
    }

    @Test
    void idUngueltigHasExpectedMessage() {
        assertEquals("Ungültige ID", DomainError.ID_UNGUELTIG.defaultMessage());
    }
}
