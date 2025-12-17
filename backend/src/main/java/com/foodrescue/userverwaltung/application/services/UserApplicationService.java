package com.foodrescue.userverwaltung.application.services;

import com.foodrescue.userverwaltung.application.commands.RegistriereUserCommand;
import com.foodrescue.userverwaltung.domain.events.UserRegistriertEvent;
import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.queries.UserDetailsQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import com.foodrescue.userverwaltung.infrastructure.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserApplicationService {

  private final UserRepository userRepository;
  private final UserDomainService userDomainService;

  public UserApplicationService(
      UserRepository userRepository, UserDomainService userDomainService) {
    this.userRepository = userRepository;
    this.userDomainService = userDomainService;
  }

  public UserDetailsQuery registriereUser(RegistriereUserCommand command) {
    EmailAdresse email = new EmailAdresse(command.getEmail().toString());

    userRepository
        .findeMitEmail(email)
        .ifPresent(
            u -> {
              throw new IllegalArgumentException("E-Mail ist bereits vergeben");
            });

    // Korrektur: UserId mit UUID erstellen
    UserId userId = new UserId(UUID.randomUUID());
    Name name = new Name(command.getName().toString());

    User user = userDomainService.registriereUser(userId, name, email, command.getRolle());

    User gespeicherterUser = userRepository.speichern(user);

    UserRegistriertEvent event =
        new UserRegistriertEvent(
            gespeicherterUser.getId(),
            gespeicherterUser.getName(),
            gespeicherterUser.getEmail(),
            gespeicherterUser.getRolle());
    // z.B. domainEventPublisher.publish(event);

    return new UserDetailsQuery(
        gespeicherterUser.getId(),
        gespeicherterUser.getName(),
        gespeicherterUser.getEmail(),
        gespeicherterUser.getRolle());
  }

  public UserDetailsQuery findeUserByEmail(EmailAdresse emailAdresse) {
    User user =
        userRepository
            .findeMitEmail(emailAdresse)
            .orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));
    return new UserDetailsQuery(user.getId(), user.getName(), user.getEmail(), user.getRolle());
  }
}
