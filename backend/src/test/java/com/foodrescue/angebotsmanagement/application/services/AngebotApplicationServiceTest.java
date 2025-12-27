package com.foodrescue.angebotsmanagement.application.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.foodrescue.angebotsmanagement.application.commands.ErstelleAngebotCommand;
import com.foodrescue.angebotsmanagement.application.commands.VeroeffentlicheAngebotCommand;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AngebotApplicationServiceTest {

  @Mock AngebotRepository repository;

  AngebotDomainService domainService = new AngebotDomainService();

  @InjectMocks AngebotApplicationService service;

  @Captor ArgumentCaptor<Angebot> angebotCaptor;

  private static ErstelleAngebotCommand validCmd() {
    ErstelleAngebotCommand cmd = new ErstelleAngebotCommand();
    cmd.setAnbieterId(UUID.randomUUID().toString());
    cmd.setTitel("Titel");
    cmd.setBeschreibung("Beschreibung");
    cmd.setTags(Set.of("t1"));
    cmd.setVon("2025-12-14T10:00");
    cmd.setBis("2025-12-14T12:00");
    return cmd;
  }

  @Test
  void erstelleAngebot_happyPath_persistsAggregate_andReturnsId() {
    // ensure InjectMocks uses our real domainService
    service = new AngebotApplicationService(repository, domainService);

    ErstelleAngebotCommand cmd = validCmd();

    AngebotsId id = service.erstelleAngebot(cmd);

    assertNotNull(id);
    verify(repository).speichern(angebotCaptor.capture());

    Angebot angebot = angebotCaptor.getValue();
    assertNotNull(angebot);
    assertEquals(id.value(), angebot.getId());
    assertEquals("ENTWURF", angebot.getStatus().name());
    assertEquals(cmd.getTitel(), angebot.getTitel());
    assertEquals(cmd.getBeschreibung(), angebot.getBeschreibung());
    assertEquals(cmd.getTags(), angebot.getTags());
    assertNotNull(angebot.getZeitfenster());
  }

  @Test
  void erstelleAngebot_invalidAnbieterId_throws() {
    service = new AngebotApplicationService(repository, domainService);

    ErstelleAngebotCommand cmd = validCmd();
    cmd.setAnbieterId("not-a-uuid");

    assertThrows(IllegalArgumentException.class, () -> service.erstelleAngebot(cmd));
    verify(repository, never()).speichern(any());
  }

  @Test
  void erstelleAngebot_invalidVonBisFormat_throws() {
    service = new AngebotApplicationService(repository, domainService);

    ErstelleAngebotCommand cmd = validCmd();
    cmd.setVon("14.12.2025 10:00");
    cmd.setBis("14.12.2025 12:00");

    assertThrows(RuntimeException.class, () -> service.erstelleAngebot(cmd));
    verify(repository, never()).speichern(any());
  }

  @Test
  void veroeffentlicheAngebot_notFound_throws() {
    service = new AngebotApplicationService(repository, domainService);

    AngebotsId id = AngebotsId.of("missing");
    when(repository.findeMitId(id)).thenReturn(Optional.empty());

    assertThrows(
        IllegalArgumentException.class,
        () -> service.veroeffentlicheAngebot(new VeroeffentlicheAngebotCommand(id)));
    verify(repository, never()).speichern(any());
  }

  @Test
  void veroeffentlicheAngebot_happyPath_savesUpdatedAggregate() {
    service = new AngebotApplicationService(repository, domainService);

    // create ENTWURF offer
    var cmd = validCmd();
    var createdId = AngebotsId.of("a1");
    var angebot =
        domainService.erstelleAngebot(
            createdId,
            new com.foodrescue.userverwaltung.domain.valueobjects.UserId(UUID.randomUUID()),
            cmd.getTitel(),
            cmd.getBeschreibung(),
            cmd.getTags(),
            new com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster(
                java.time.LocalDateTime.parse(cmd.getVon()),
                java.time.LocalDateTime.parse(cmd.getBis())));

    when(repository.findeMitId(createdId)).thenReturn(Optional.of(angebot));

    service.veroeffentlicheAngebot(new VeroeffentlicheAngebotCommand(createdId));

    assertEquals(Angebot.Status.VERFUEGBAR, angebot.getStatus());
    verify(repository).speichern(angebot);
  }
}
