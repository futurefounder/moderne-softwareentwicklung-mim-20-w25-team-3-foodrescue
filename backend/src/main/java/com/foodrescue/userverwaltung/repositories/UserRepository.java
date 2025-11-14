package com.foodrescue.userverwaltung.repositories;

import com.foodrescue.userverwaltung.domain.User;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findeMitId(UserId id);

  void speichern(User user);
}
