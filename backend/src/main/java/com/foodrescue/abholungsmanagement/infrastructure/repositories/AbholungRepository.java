package com.foodrescue.abholungsmanagement.infrastructure.repositories;

import com.foodrescue.abholungsmanagement.domain.model.Abholung;
import java.util.Optional;

public interface AbholungRepository {
  Abholung speichern(Abholung abholung);

  Optional<Abholung> findeMitId(String id);
}
