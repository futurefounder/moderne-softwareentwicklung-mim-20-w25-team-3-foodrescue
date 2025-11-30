package com.foodrescue.userverwaltung.domain.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

public final class EmailAdresse {

  private static final Pattern SIMPLE_PATTERN = Pattern.compile(".+@.+\\..+");

  private final String value;

  public EmailAdresse(String value) {
    Objects.requireNonNull(value, "Email darf nicht null sein");
    String trimmed = value.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("Email darf nicht leer sein");
    }
    if (!SIMPLE_PATTERN.matcher(trimmed).matches()) {
      throw new IllegalArgumentException("Email hat ein ungültiges Format: " + trimmed);
    }
    this.value = trimmed;
  }

  public String getValue() {
    return value;
  }

  public String getDomain() {
    int idx = value.indexOf('@');
    return value.substring(idx + 1);
  }

  @Override
  public String toString() {
    return value;
  }

  // equals / hashCode
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EmailAdresse)) return false;
    EmailAdresse that = (EmailAdresse) o;
    return value.equalsIgnoreCase(that.value);
  }

  @Override
  public int hashCode() {
    return value.toLowerCase().hashCode();
  }
}
