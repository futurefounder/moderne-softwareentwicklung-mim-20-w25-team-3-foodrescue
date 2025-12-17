package com.foodrescue.abholungsmanagement.infrastructure.persistence;

import com.foodrescue.abholungsmanagement.domain.model.Abholung;
import com.foodrescue.abholungsmanagement.infrastructure.repositories.AbholungRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAbholungRepository implements AbholungRepository {

    private final Map<String, Abholung> store = new ConcurrentHashMap<>();

    @Override
    public Abholung speichern(Abholung abholung) {
        store.put(abholung.getId(), abholung);
        return abholung;
    }

    @Override
    public Optional<Abholung> findeMitId(String id) {
        return Optional.ofNullable(store.get(id));
    }
}
