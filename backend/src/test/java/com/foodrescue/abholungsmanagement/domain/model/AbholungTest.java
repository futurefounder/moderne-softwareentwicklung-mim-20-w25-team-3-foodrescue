package com.foodrescue.abholungsmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.abholungsmanagement.domain.events.AbholungAbgeschlossen;
import com.foodrescue.abholungsmanagement.domain.events.AbholungFehlgeschlagenEvent;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.shared.exception.DomainException;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class AbholungTest {

  @Test
  void constructor_rejectsIncompleteAggregate() {
    Abholcode code = Abholcode.of("AB12");
    assertAll(
        () -> assertThrows(DomainException.class, () -> new Abholung(null, "r1", code)),
        () -> assertThrows(DomainException.class, () -> new Abholung("a1", null, code)),
        () -> assertThrows(DomainException.class, () -> new Abholung("a1", "r1", null)));
  }

  @Test
  void bestaetigen_happyPath_setsStatusAndReturnsEvent_andSetsTimestamp() {
    Abholcode code = Abholcode.of("AB12");
    Abholung abholung = new Abholung("a1", "r1", code);

    Instant before = Instant.now();
    List<DomainEvent> events = abholung.bestaetigen(Abholcode.of("AB12"));
    Instant after = Instant.now();

    assertEquals(Abholung.Status.ABGESCHLOSSEN, abholung.getStatus());
    assertNotNull(abholung.getAbgeschlossenAm());
    assertFalse(abholung.getAbgeschlossenAm().isBefore(before));
    assertFalse(abholung.getAbgeschlossenAm().isAfter(after));

    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof AbholungAbgeschlossen);
    AbholungAbgeschlossen e = (AbholungAbgeschlossen) events.get(0);
    assertEquals("r1", e.getReservierungsId());
    assertNotNull(e.occurredOn());
  }

  @Test
  void bestaetigen_wrongCode_setsStatusToFailed_andThrows() {
    Abholcode correct = Abholcode.of("AB12");
    Abholung abholung = new Abholung("a1", "r1", correct);

    DomainException ex =
        assertThrows(DomainException.class, () -> abholung.bestaetigen(Abholcode.of("CD34")));
    assertEquals("Falscher Abholcode", ex.getMessage());

    // Edge case: Statusänderung trotz Exception
    assertEquals(Abholung.Status.FEHLGESCHLAGEN, abholung.getStatus());
    assertNull(abholung.getAbgeschlossenAm());

    // Weitere Verarbeitung danach ist verboten
    assertThrows(DomainException.class, () -> abholung.bestaetigen(correct));
    assertThrows(DomainException.class, abholung::fehlschlagen);
  }

  @Test
  void bestaetigen_cannotBeCalledTwice() {
    Abholcode code = Abholcode.of("AB12");
    Abholung abholung = new Abholung("a1", "r1", code);

    abholung.bestaetigen(code);
    DomainException ex = assertThrows(DomainException.class, () -> abholung.bestaetigen(code));
    assertEquals("Abholung wurde bereits verarbeitet", ex.getMessage());
  }

  @Test
  void fehlschlagen_setsFailedAndReturnsEvent() {
    Abholcode code = Abholcode.of("AB12");
    Abholung abholung = new Abholung("a1", "r1", code);

    List<DomainEvent> events = abholung.fehlschlagen();

    assertEquals(Abholung.Status.FEHLGESCHLAGEN, abholung.getStatus());
    assertNull(abholung.getAbgeschlossenAm());

    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof AbholungFehlgeschlagenEvent);
    AbholungFehlgeschlagenEvent e = (AbholungFehlgeschlagenEvent) events.get(0);
    assertEquals("r1", e.getReservierungsId());
    assertNotNull(e.occurredOn());
  }

  @Test
  void fehlschlagen_cannotBeCalledAfterCompletion() {
    Abholcode code = Abholcode.of("AB12");
    Abholung abholung = new Abholung("a1", "r1", code);

    abholung.bestaetigen(code);
    assertThrows(DomainException.class, abholung::fehlschlagen);
  }
}
