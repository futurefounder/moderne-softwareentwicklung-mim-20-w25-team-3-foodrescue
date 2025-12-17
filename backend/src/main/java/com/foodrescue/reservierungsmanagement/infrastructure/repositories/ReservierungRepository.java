package com.foodrescue.reservierungsmanagement.infrastructure.repositories;

import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import java.util.List;
import java.util.Optional;

public interface ReservierungRepository {
  Reservierung speichern(Reservierung reservierung);

  Optional<Reservierung> findeMitId(String id);

  List<Reservierung> findeFuerAbholer(String abholerId);
}
