package com.foodrescue.domain.model;

import com.foodrescue.exceptions.DomainException;
import java.security.SecureRandom;

public final class Abholcode {
  private final String value;

  private Abholcode(String value) {
    if (value == null || !value.matches("[A-Z0-9]{4,8}"))
      throw new DomainException("Abholcode muss 4-8 Zeichen [A-Z0-9] sein");
    this.value = value;
  }

  public static Abholcode of(String value) {
    return new Abholcode(value);
  }

  public static Abholcode random() {
    var r = new SecureRandom();
    var alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    var sb = new StringBuilder();
    for (int i = 0; i < 6; i++) sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
    return new Abholcode(sb.toString());
  }

  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof Abholcode c && value.equals(c.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
