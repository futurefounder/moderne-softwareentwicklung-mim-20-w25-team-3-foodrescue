package com.foodrescue.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.foodrescue.domain.events.AbholungAbgeschlossen;
import com.foodrescue.domain.events.DomainEvent;
import com.foodrescue.exceptions.DomainException;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AbholungTest {

  @Test
  void bestaetigen_mitKorrektCode_setztStatusAbgeschlossen_und_emittiertEvent() {
    var code = Abholcode.of("XZ12");
    var abholung = new Abholung("h1", "r1", code);

    List<?> events = abholung.bestaetigen(Abholcode.of("XZ12"));

    assertEquals(Abholung.Status.ABGESCHLOSSEN, abholung.getStatus());
    assertTrue(events.stream().anyMatch(e -> e instanceof AbholungAbgeschlossen));
    assertNotNull(abholung.getAbgeschlossenAm());
  }

  @Test
  void bestaetigen_mitFalschemCode_setztStatusFehlgeschlagen_und_wirftFehler() {
    var abholung = new Abholung("h1", "r1", Abholcode.of("AB12"));

    var ex = assertThrows(DomainException.class, () -> abholung.bestaetigen(Abholcode.of("ZZ99")));

    assertEquals("Falscher Abholcode", ex.getMessage());
    assertEquals(Abholung.Status.FEHLGESCHLAGEN, abholung.getStatus());
  }

  @Test
  void doppelteBestaetigung_wirftFehler() {
    var code = Abholcode.of("AB12");
    var abholung = new Abholung("h1", "r1", code);
    abholung.bestaetigen(code);

    assertThrows(DomainException.class, () -> abholung.bestaetigen(code));
  }

  @Test
  void constructorRejectsNulls() {
    assertThrows(DomainException.class, () -> new Abholung(null, "r1", Abholcode.of("AB12")));
    assertThrows(DomainException.class, () -> new Abholung("h1", null, Abholcode.of("AB12")));
    assertThrows(DomainException.class, () -> new Abholung("h1", "r1", null));
  }

  @Test
  void gettersReturnCorrectValues() {
    var code = Abholcode.of("AB12");
    var a = new Abholung("h1", "r1", code);

    assertEquals("h1", a.getId());
    assertEquals("r1", a.getReservierungsId());
    assertEquals(Abholung.Status.ANGELEGT, a.getStatus());
  }

  @Test
  void constructorInitializesFieldsAndDefaultStatus() {
    Abholcode code = Abholcode.of("AB12CD");
    Abholung abholung = new Abholung("h1", "r1", code);

    assertEquals("h1", abholung.getId());
    assertEquals("r1", abholung.getReservierungsId());
    assertEquals(code, abholung.getAbholcode());
    assertEquals(Abholung.Status.ANGELEGT, abholung.getStatus());
    assertNotNull(abholung.getAngelegtAm());
    assertNull(abholung.getAbgeschlossenAm());
  }

  @Test
  void constructorRejectsNullId() {
    Abholcode code = Abholcode.of("AB12CD");
    assertThrows(DomainException.class, () -> new Abholung(null, "r1", code));
  }

  @Test
  void constructorRejectsNullReservierungsId() {
    Abholcode code = Abholcode.of("AB12CD");
    assertThrows(DomainException.class, () -> new Abholung("h1", null, code));
  }

  @Test
  void constructorRejectsNullAbholcode() {
    assertThrows(DomainException.class, () -> new Abholung("h1", "r1", null));
  }

  @Test
  void bestaetigenWithCorrectCodeChangesStatusAndCreatesEvent() {
    Abholcode code = Abholcode.of("AB12CD");
    Abholung abholung = new Abholung("h1", "r1", code);

    Instant before = Instant.now();
    List<DomainEvent> events = abholung.bestaetigen(code);
    Instant after = Instant.now();

    // Status & Zeit
    assertEquals(Abholung.Status.ABGESCHLOSSEN, abholung.getStatus());
    assertNotNull(abholung.getAbgeschlossenAm());
    assertTrue(
        !abholung.getAbgeschlossenAm().isBefore(before)
            && !abholung.getAbgeschlossenAm().isAfter(after));

    // Event
    assertEquals(1, events.size());
    assertTrue(events.get(0) instanceof AbholungAbgeschlossen);
    AbholungAbgeschlossen event = (AbholungAbgeschlossen) events.get(0);
    assertEquals("r1", event.getReservierungsId());
  }

  @Test
  void bestaetigenWithWrongCodeSetsStatusFehlgeschlagenAndThrows() {
    Abholcode correct = Abholcode.of("AB12CD");
    Abholcode wrong = Abholcode.of("ZZ99YY");
    Abholung abholung = new Abholung("h1", "r1", correct);

    DomainException ex = assertThrows(DomainException.class, () -> abholung.bestaetigen(wrong));

    assertEquals("Falscher Abholcode", ex.getMessage());
    assertEquals(Abholung.Status.FEHLGESCHLAGEN, abholung.getStatus());
    assertNull(abholung.getAbgeschlossenAm());
  }

  @Test
  void bestaetigenAfterSuccessThrowsAlreadyProcessed() {
    Abholcode code = Abholcode.of("AB12CD");
    Abholung abholung = new Abholung("h1", "r1", code);

    // erster erfolgreicher Versuch
    abholung.bestaetigen(code);

    // zweiter Versuch
    DomainException ex = assertThrows(DomainException.class, () -> abholung.bestaetigen(code));

    assertEquals("Abholung wurde bereits verarbeitet", ex.getMessage());
    assertEquals(Abholung.Status.ABGESCHLOSSEN, abholung.getStatus());
  }

  @Test
  void bestaetigenAfterFailedAttemptThrowsAlreadyProcessed() {
    Abholcode correct = Abholcode.of("AB12CD");
    Abholcode wrong = Abholcode.of("ZZ99YY");
    Abholung abholung = new Abholung("h1", "r1", correct);

    // erster Versuch: falscher Code → FEHLGESCHLAGEN
    assertThrows(DomainException.class, () -> abholung.bestaetigen(wrong));
    assertEquals(Abholung.Status.FEHLGESCHLAGEN, abholung.getStatus());

    // zweiter Versuch egal mit welchem Code
    DomainException ex = assertThrows(DomainException.class, () -> abholung.bestaetigen(correct));

    assertEquals("Abholung wurde bereits verarbeitet", ex.getMessage());
    assertEquals(Abholung.Status.FEHLGESCHLAGEN, abholung.getStatus());
  }
}
