package com.foodrescue.userverwaltung.persistence;

import com.foodrescue.userverwaltung.domain.User;
import com.foodrescue.userverwaltung.repositories.UserRepository;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

  private final Map<UserId, User> byId = new ConcurrentHashMap<>();

  @Override
  public Optional<User> findeMitId(UserId id) {
    return Optional.ofNullable(byId.get(id));
  }

  @Override
  public void speichern(User user) {
    byId.put(user.getId(), user);
  }
}
