package com.foodrescue.reservierungsmanagement.infrastructure.persistence;

import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.infrastructure.repositories.ReservierungRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryReservierungRepository implements ReservierungRepository {

  private final Map<String, Reservierung> store = new ConcurrentHashMap<>();

  @Override
  public Reservierung speichern(Reservierung reservierung) {
    store.put(reservierung.getId(), reservierung);
    return reservierung;
  }

  @Override
  public Optional<Reservierung> findeMitId(String id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Reservierung> findeFuerAbholer(String abholerId) {
    return store.values().stream()
        .filter(r -> r.getAbholerId().equals(abholerId))
        .collect(Collectors.toList());
  }
}
