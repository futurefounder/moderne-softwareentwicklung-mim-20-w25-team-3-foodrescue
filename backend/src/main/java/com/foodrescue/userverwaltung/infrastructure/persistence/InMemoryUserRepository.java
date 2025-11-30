package com.foodrescue.userverwaltung.infrastructure.persistence;

import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import com.foodrescue.userverwaltung.infrastructure.repositories.UserRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<UserId, User> userStore = new ConcurrentHashMap<>();
    private final Map<EmailAdresse, UserId> emailIndex = new ConcurrentHashMap<>();

    @Override
    public User speichern(User user) {
        userStore.put(user.getId(), user);
        emailIndex.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
    public Optional<User> findeMitId(UserId id) {
        return Optional.ofNullable(userStore.get(id));
    }

    @Override
    public Optional<User> findeMitEmail(EmailAdresse email) {
        UserId id = emailIndex.get(email);
        if (id == null) {
            return Optional.empty();
        }
        return findeMitId(id);
    }
}
