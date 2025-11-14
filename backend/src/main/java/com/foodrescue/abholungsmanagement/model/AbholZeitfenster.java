package com.foodrescue.abholungsmanagement.model;

import com.foodrescue.shared.exception.DomainException;
import java.time.LocalDateTime;
import java.util.Objects;

public final class AbholZeitfenster {
  private final LocalDateTime von;
  private final LocalDateTime bis;

  private AbholZeitfenster(LocalDateTime von, LocalDateTime bis) {
    if (von == null || bis == null || !bis.isAfter(von)) {
      throw new DomainException("Ungültiges Zeitfenster");
    }
    this.von = von;
    this.bis = bis;
  }

  public static AbholZeitfenster of(LocalDateTime von, LocalDateTime bis) {
    return new AbholZeitfenster(von, bis);
  }

  public boolean istNochAktuell(LocalDateTime jetzt) {
    return jetzt.isBefore(bis);
  }

  public LocalDateTime von() {
    return von;
  }

  public LocalDateTime bis() {
    return bis;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof AbholZeitfenster z && von.equals(z.von) && bis.equals(z.bis);
  }

  @Override
  public int hashCode() {
    return Objects.hash(von, bis);
  }
}
