package com.foodrescue.userverwaltung.application.services;

import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

  public User registriereUser(UserId id, Name name, EmailAdresse email, Rolle rolle) {
    // hier ggf. weitere fachliche Regeln
    return User.registrieren(id, name, email, rolle);
  }
}
