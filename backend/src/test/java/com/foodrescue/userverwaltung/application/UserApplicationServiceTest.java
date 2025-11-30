package com.foodrescue.userverwaltung.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.userverwaltung.application.commands.RegistriereUserCommand;
import com.foodrescue.userverwaltung.application.services.UserApplicationService;
import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.infrastructure.repositories.UserRepository;
import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserApplicationServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserApplicationService userApplicationService;

  private RegistriereUserCommand command;

  @BeforeEach
  void setUp() {
    command = new RegistriereUserCommand("Max Mustermann", "max@example.com", Rolle.ABHOLER);
  }

  @Test
  void registriereUserSpeichertUserImRepository() {
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    UserId id = userApplicationService.registriereUser(command);

    assertNotNull(id);
    verify(userRepository, times(1)).speichern(userCaptor.capture());

    User gespeicherterUser = userCaptor.getValue();
    assertEquals("Max Mustermann", gespeicherterUser.getName().getValue());
    assertEquals("max@example.com", gespeicherterUser.getEmail().getValue());
    assertEquals(Rolle.ABHOLER, gespeicherterUser.getRolle());
    assertNotNull(gespeicherterUser.getId());
  }

  @Test
  void holeUserDetailsGibtGefundenenUserZurueck() {
    UUID uuid = UUID.randomUUID();
    UserId userId = new UserId(uuid);
    User user =
        new User(userId, new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ABHOLER);

    when(userRepository.findeMitId(userId)).thenReturn(Optional.of(user));

    User result = userApplicationService.holeUserDetails(uuid);

    assertEquals(user, result);
    verify(userRepository, times(1)).findeMitId(userId);
  }

  @Test
  void holeUserDetailsWirftExceptionWennUserNichtGefunden() {
    UUID uuid = UUID.randomUUID();
    UserId userId = new UserId(uuid);

    when(userRepository.findeMitId(userId)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementException.class, () -> userApplicationService.holeUserDetails(uuid));

    verify(userRepository, times(1)).findeMitId(userId);
  }
}
