package com.foodrescue.anbieterverwaltung.repositories;

import com.foodrescue.anbieterverwaltung.domain.AnbieterProfil;
import com.foodrescue.anbieterverwaltung.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.List;
import java.util.Optional;

public interface AnbieterProfilRepository {

  Optional<AnbieterProfil> findeMitId(AnbieterProfilId id);

  Optional<AnbieterProfil> findeFuerUser(UserId userId);

  List<AnbieterProfil> findeAlle();

  void speichern(AnbieterProfil profil);
}
