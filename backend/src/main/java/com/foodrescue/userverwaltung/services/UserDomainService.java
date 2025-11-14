package com.foodrescue.userverwaltung.services;

import com.foodrescue.userverwaltung.domain.User;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

  // Beispiel: später könnten hier Regeln wie "darf Rolle wechseln" etc. landen
  public boolean darfZuAnbieterWerden(User user) {
    // aktuell immer true, später z.B. anhand Reputation / Verifizierung prüfen
    return true;
  }
}
