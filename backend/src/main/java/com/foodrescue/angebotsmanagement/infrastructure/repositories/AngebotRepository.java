package com.foodrescue.angebotsmanagement.infrastructure.repositories;

import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import java.util.List;
import java.util.Optional;

public interface AngebotRepository {
  Angebot speichern(Angebot angebot);

  Optional<Angebot> findeMitId(AngebotsId id);

  List<Angebot> findeAlleVerfuegbar();

  List<Angebot> findeFuerAnbieter(String anbieterId);
}
