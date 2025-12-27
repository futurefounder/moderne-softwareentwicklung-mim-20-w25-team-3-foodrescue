package com.foodrescue.userverwaltung.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.foodrescue.userverwaltung.application.commands.RegistriereUserCommand;
import com.foodrescue.userverwaltung.domain.model.User;
import com.foodrescue.userverwaltung.domain.queries.UserDetailsQuery;
import com.foodrescue.userverwaltung.domain.valueobjects.*;
import com.foodrescue.userverwaltung.infrastructure.repositories.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserDomainService userDomainService;

  @InjectMocks private UserApplicationService service;

  @Test
  void registriereUser_rejectsDuplicateEmail() {
    EmailAdresse email = new EmailAdresse("max@example.com");
    when(userRepository.findeMitEmail(any(EmailAdresse.class)))
        .thenReturn(Optional.of(mock(User.class)));

    RegistriereUserCommand cmd = new RegistriereUserCommand(new Name("Max"), email, Rolle.ABHOLER);

    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> service.registriereUser(cmd));
    assertTrue(ex.getMessage().contains("bereits vergeben"));

    verify(userDomainService, never()).registriereUser(any(), any(), any(), any());
    verify(userRepository, never()).speichern(any());
  }

  @Test
  void registriereUser_success_persistsAndReturnsQuery() {
    when(userRepository.findeMitEmail(any(EmailAdresse.class))).thenReturn(Optional.empty());

    // Domain service returns a user instance (with id set by app-service)
    ArgumentCaptor<UserId> idCaptor = ArgumentCaptor.forClass(UserId.class);
    when(userDomainService.registriereUser(
            idCaptor.capture(), any(Name.class), any(EmailAdresse.class), any(Rolle.class)))
        .thenAnswer(
            inv ->
                User.registrieren(
                    inv.getArgument(0),
                    inv.getArgument(1),
                    inv.getArgument(2),
                    inv.getArgument(3)));

    when(userRepository.speichern(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

    RegistriereUserCommand cmd =
        new RegistriereUserCommand(
            new Name("Max"), new EmailAdresse("max@example.com"), Rolle.ABHOLER);

    UserDetailsQuery result = service.registriereUser(cmd);

    assertNotNull(result.getUserId());
    assertEquals("Max", result.getName().getValue());
    assertEquals("max@example.com", result.getEmail().getValue());
    assertEquals(Rolle.ABHOLER, result.getRolle());

    // Ensure application service generated an ID
    assertNotNull(idCaptor.getValue());
    assertNotNull(idCaptor.getValue().getValue());

    verify(userRepository).speichern(any(User.class));
  }

  @Test
  void findeUserByEmail_throwsWhenMissing() {
    when(userRepository.findeMitEmail(any(EmailAdresse.class))).thenReturn(Optional.empty());

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> service.findeUserByEmail(new EmailAdresse("x@y.de")));
    assertTrue(ex.getMessage().contains("nicht gefunden"));
  }

  @Test
  void findeUserByEmail_returnsQuery() {
    User user =
        User.registrieren(
            new UserId(UUID.randomUUID()),
            new Name("Max"),
            new EmailAdresse("max@example.com"),
            Rolle.ABHOLER);
    when(userRepository.findeMitEmail(any(EmailAdresse.class))).thenReturn(Optional.of(user));

    UserDetailsQuery q = service.findeUserByEmail(new EmailAdresse("max@example.com"));

    assertEquals(user.getId(), q.getUserId());
    assertEquals(user.getEmail(), q.getEmail());
    assertEquals(user.getName(), q.getName());
    assertEquals(user.getRolle(), q.getRolle());
  }
}
