package com.foodrescue.userverwaltung.application.services;

import com.foodrescue.userverwaltung.domain.model.AnbieterProfil;
import com.foodrescue.userverwaltung.domain.valueobjects.Adresse;
import com.foodrescue.userverwaltung.domain.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.domain.valueobjects.GeoStandort;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftsname;
import com.foodrescue.userverwaltung.domain.valueobjects.Geschaeftstyp;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import org.springframework.stereotype.Service;

@Service
public class AnbieterDomainService {

  public AnbieterProfil erstelleAnbieterProfil(
      AnbieterProfilId id,
      UserId userId,
      Rolle userRolle,
      Geschaeftsname geschaeftsname,
      Geschaeftstyp geschaeftstyp,
      Adresse adresse,
      GeoStandort geoStandort) {

    return AnbieterProfil.erstellenFuerAnbieter(
        id, userId, userRolle, geschaeftsname, geschaeftstyp, adresse, geoStandort);
  }
}
