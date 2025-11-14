package com.foodrescue.userverwaltung.valueobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NameTest {

  @Test
  void erstelltNameMitTrim() {
    Name name = new Name("  Max  ");
    assertEquals("Max", name.getValue());
    assertEquals("Max", name.toString());
  }

  @Test
  void wirftExceptionBeiNull() {
    assertThrows(NullPointerException.class, () -> new Name(null));
  }

  @Test
  void wirftExceptionBeiLeer() {
    assertThrows(IllegalArgumentException.class, () -> new Name("   "));
  }

  @Test
  void equalsUndHashCode() {
    Name a = new Name("Max");
    Name b = new Name("Max");
    Name c = new Name("Moritz");

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
    assertNotEquals(a, c);
  }
}
