package com.foodrescue.userverwaltung.infrastructure.repositories;

import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.Optional;

public interface UserRepository {

  User speichern(User user);

  Optional<User> findeMitId(UserId id);

  Optional<User> findeMitEmail(EmailAdresse email);
}
