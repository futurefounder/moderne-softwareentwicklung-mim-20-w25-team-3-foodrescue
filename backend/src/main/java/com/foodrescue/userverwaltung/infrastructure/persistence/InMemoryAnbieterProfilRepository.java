package com.foodrescue.userverwaltung.infrastructure.persistence;

import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import com.foodrescue.userverwaltung.infrastructure.repositories.AnbieterProfilRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAnbieterProfilRepository implements AnbieterProfilRepository {

    private final Map<AnbieterProfilId, AnbieterProfil> profilStore = new ConcurrentHashMap<>();
    private final Map<UserId, AnbieterProfilId> userIndex = new ConcurrentHashMap<>();

    @Override
    public AnbieterProfil speichern(AnbieterProfil profil) {
        profilStore.put(profil.getId(), profil);
        userIndex.put(profil.getUserId(), profil.getId());
        return profil;
    }

    @Override
    public Optional<AnbieterProfil> findeMitId(AnbieterProfilId id) {
        return Optional.ofNullable(profilStore.get(id));
    }

    @Override
    public Optional<AnbieterProfil> findeFuerUser(UserId userId) {
        AnbieterProfilId profilId = userIndex.get(userId);
        if (profilId == null) {
            return Optional.empty();
        }
        return findeMitId(profilId);
    }
}
