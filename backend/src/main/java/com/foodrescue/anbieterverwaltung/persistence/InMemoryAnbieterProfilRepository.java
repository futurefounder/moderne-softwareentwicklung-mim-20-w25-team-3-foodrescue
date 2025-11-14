package com.foodrescue.anbieterverwaltung.persistence;

import com.foodrescue.anbieterverwaltung.domain.AnbieterProfil;
import com.foodrescue.anbieterverwaltung.repositories.AnbieterProfilRepository;
import com.foodrescue.anbieterverwaltung.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAnbieterProfilRepository implements AnbieterProfilRepository {

  private final Map<AnbieterProfilId, AnbieterProfil> byId = new ConcurrentHashMap<>();

  @Override
  public Optional<AnbieterProfil> findeMitId(AnbieterProfilId id) {
    return Optional.ofNullable(byId.get(id));
  }

  @Override
  public Optional<AnbieterProfil> findeFuerUser(UserId userId) {
    return byId.values().stream().filter(p -> p.getUserId().equals(userId)).findFirst();
  }

  @Override
  public List<AnbieterProfil> findeAlle() {
    return new ArrayList<>(byId.values());
  }

  @Override
  public void speichern(AnbieterProfil profil) {
    byId.put(profil.getId(), profil);
  }
}
