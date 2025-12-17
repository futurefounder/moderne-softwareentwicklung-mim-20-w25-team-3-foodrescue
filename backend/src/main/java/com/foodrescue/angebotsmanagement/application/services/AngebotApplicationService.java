package com.foodrescue.angebotsmanagement.application.services;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.application.commands.ErstelleAngebotCommand;
import com.foodrescue.angebotsmanagement.application.commands.VeroeffentlicheAngebotCommand;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import com.foodrescue.shared.domain.DomainEvent;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AngebotApplicationService {

  private final AngebotRepository repository;
  private final AngebotDomainService domainService;

  public AngebotApplicationService(
      AngebotRepository repository, AngebotDomainService domainService) {
    this.repository = repository;
    this.domainService = domainService;
  }

  public AngebotsId erstelleAngebot(ErstelleAngebotCommand cmd) {

    // UserId aus UUID-String
    UserId anbieterId =
        new UserId(UUID.fromString(cmd.getAnbieterId())); // <- passt zu Ihrer UserId
    // :contentReference[oaicite:5]{index=5}

    // Zeitfenster aus ISO-Strings
    LocalDateTime von = LocalDateTime.parse(cmd.getVon());
    LocalDateTime bis = LocalDateTime.parse(cmd.getBis());

    AbholZeitfenster zeitfenster = new AbholZeitfenster(von, bis);

    AngebotsId id =
        AngebotsId.of(UUID.randomUUID().toString()); // oder Ihre bestehende Id-Erzeugung
    Angebot angebot =
        domainService.erstelleAngebot(
            id, anbieterId, cmd.getTitel(), cmd.getBeschreibung(), cmd.getTags(), zeitfenster);

    repository.speichern(angebot);
    return id;
  }

  public void veroeffentlicheAngebot(VeroeffentlicheAngebotCommand command) {
    Optional<Angebot> optionalAngebot = repository.findeMitId(command.getAngebotId());
    Angebot angebot =
        optionalAngebot.orElseThrow(() -> new IllegalArgumentException("Angebot nicht gefunden"));
    List<DomainEvent> events = angebot.veroeffentlichen();
    repository.speichern(angebot);
    // Publish events
  }

  public List<Angebot> findeAngeboteFuerAnbieter(String anbieterId) {
    return repository.findeFuerAnbieter(anbieterId);
  }

  public Angebot findeAngebotMitId(AngebotsId id) {
    return repository
        .findeMitId(id)
        .orElseThrow(() -> new IllegalArgumentException("Angebot nicht gefunden"));
  }

  public List<Angebot> findeVerfuegbareAngebote() {
    return repository.findeAlleVerfuegbar();
  }
}
