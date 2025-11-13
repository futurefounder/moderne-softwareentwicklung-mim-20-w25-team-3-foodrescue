package com.foodrescue.domain.model.ids;

import com.foodrescue.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnbieterIdTest {

    @Test
    void createsValidId() {
        AnbieterId id = AnbieterId.of("anb-123");

        assertEquals("anb-123", id.value());
        assertEquals("anb-123", id.toString());
    }

    @Test
    void equalIdsAreEqualAndHaveSameHashCode() {
        AnbieterId id1 = AnbieterId.of("anb-123");
        AnbieterId id2 = AnbieterId.of("anb-123");
        AnbieterId idOther = AnbieterId.of("anb-456");

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1, idOther);
        assertNotEquals(null, id1);
        assertNotEquals("some string", id1);
    }

    @Test
    void nullValueIsRejected() {
        assertThrows(DomainException.class, () -> AnbieterId.of(null));
    }

    @Test
    void blankValueIsRejected() {
        assertThrows(DomainException.class, () -> AnbieterId.of("   "));
    }
}

