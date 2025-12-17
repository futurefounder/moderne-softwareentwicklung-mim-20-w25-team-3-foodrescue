package com.foodrescue.angebotsmanagement.infrastructure.persistence;

import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAngebotRepository implements AngebotRepository {

    private final Map<String, Angebot> store = new ConcurrentHashMap<>();

    @Override
    public Angebot speichern(Angebot angebot) {
        store.put(angebot.getId(), angebot);
        return angebot;
    }

    @Override
    public Optional<Angebot> findeMitId(AngebotsId id) {
        return Optional.ofNullable(store.get(id.value()));
    }

    @Override
    public List<Angebot> findeAlleVerfuegbar() {
        return store.values().stream()
                .filter(a -> a.getStatus() == Angebot.Status.VERFUEGBAR)
                .collect(Collectors.toList());
    }

    @Override
    public List<Angebot> findeFuerAnbieter(String anbieterId) {
        return store.values().stream()
                .filter(a -> a.getAnbieterId().equals(anbieterId))
                .collect(Collectors.toList());
    }
}