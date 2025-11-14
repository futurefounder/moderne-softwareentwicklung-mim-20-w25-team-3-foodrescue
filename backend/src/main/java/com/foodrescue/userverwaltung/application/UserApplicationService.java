package com.foodrescue.userverwaltung.application;

import static jakarta.transaction.Transactional.TxType.SUPPORTS;

import com.foodrescue.userverwaltung.commands.RegistriereUserCommand;
import com.foodrescue.userverwaltung.domain.User;
import com.foodrescue.userverwaltung.queries.UserDetailsQuery;
import com.foodrescue.userverwaltung.repositories.UserRepository;
import com.foodrescue.userverwaltung.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.valueobjects.Name;
import com.foodrescue.userverwaltung.valueobjects.UserId;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional // Standard: REQUIRED für schreibende Methoden
public class UserApplicationService {

  private final UserRepository userRepository;

  public UserApplicationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /** Schreibender Use Case – neue Transaktion (DEFAULT = REQUIRED). */
  public UserId registriereUser(RegistriereUserCommand command) {
    UserId id = UserId.neu();
    User user =
        User.registrieren(
            id,
            new Name(command.getName()),
            new EmailAdresse(command.getEmail()),
            command.getRolle());

    userRepository.speichern(user);
    return id;
  }

  /** Lesender Use Case – nutzt vorhandene Transaktion oder läuft ohne. */
  @Transactional(SUPPORTS)
  public User holeUserDetails(UserDetailsQuery query) {
    UserId id = new UserId(query.getUserId());
    return userRepository
        .findeMitId(id)
        .orElseThrow(() -> new NoSuchElementException("User mit Id " + id + " nicht gefunden"));
  }

  /** Convenience-Variante mit direkter UUID. */
  @Transactional(SUPPORTS)
  public User holeUserDetails(UUID userId) {
    return holeUserDetails(new UserDetailsQuery(userId));
  }
}
