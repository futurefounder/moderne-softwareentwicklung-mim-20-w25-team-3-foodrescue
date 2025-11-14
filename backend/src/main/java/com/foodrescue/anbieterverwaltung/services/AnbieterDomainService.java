package com.foodrescue.anbieterverwaltung.services;

import com.foodrescue.anbieterverwaltung.domain.AnbieterProfil;
import org.springframework.stereotype.Service;

@Service
public class AnbieterDomainService {

  // Beispiel-Platzhalter für domänenspezifische Logik
  public boolean darfAdresseAendern(AnbieterProfil profil) {
    // aktuell immer true, später z.B. Sperre bei Audits etc.
    return true;
  }
}
