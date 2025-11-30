package com.foodrescue.userverwaltung.infrastructure.repositories;

import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.Optional;

public interface AnbieterProfilRepository {

    AnbieterProfil speichern(AnbieterProfil profil);

    Optional<AnbieterProfil> findeMitId(AnbieterProfilId id);

    Optional<AnbieterProfil> findeFuerUser(UserId userId);
}
