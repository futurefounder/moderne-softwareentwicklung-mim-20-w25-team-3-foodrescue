package com.foodrescue.reservierungsmanagement.application.services;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.angebotsmanagement.infrastructure.repositories.AngebotRepository;
import com.foodrescue.reservierungsmanagement.application.commands.ReserviereAngebotCommand;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;
import com.foodrescue.reservierungsmanagement.infrastructure.repositories.ReservierungRepository;
import com.foodrescue.reservierungsmanagement.infrastructure.web.rest.ReservierungController.GeplanteAbholungResponse;
import jakarta.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReservierungsApplicationService {

  private final ReservierungRepository reservierungRepository;
  private final AngebotRepository angebotRepository;

  private static final int MAX_AKTIVE_RESERVIERUNGEN_PRO_NUTZER = 3;

  public ReservierungsApplicationService(
      ReservierungRepository reservierungRepository, AngebotRepository angebotRepository) {
    this.reservierungRepository = reservierungRepository;
    this.angebotRepository = angebotRepository;
  }

  public ReservierungsId reserviereAngebot(ReserviereAngebotCommand command) {
    Optional<Angebot> optionalAngebot = angebotRepository.findeMitId(command.getAngebotId());
    Angebot angebot =
        optionalAngebot.orElseThrow(() -> new IllegalArgumentException("Angebot nicht gefunden"));

    String abholerId = command.getAbholerId().getValue().toString();

    ReservierungsService domainService =
        new ReservierungsService(
            () -> reservierungRepository.findeFuerAbholer(abholerId).size(),
            MAX_AKTIVE_RESERVIERUNGEN_PRO_NUTZER);

    Abholcode code = Abholcode.random();
    Reservierung reservierung = domainService.reserviere(angebot, abholerId, code);

    reservierungRepository.speichern(reservierung);
    angebotRepository.speichern(angebot);

    return ReservierungsId.of(reservierung.getId());
  }

  public List<GeplanteAbholungResponse> findeGeplanteAbholungenFuerUser(String userId) {

    var formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    return reservierungRepository.findeFuerAbholer(userId).stream()
        .map(
            r -> {
              var angebotOpt = angebotRepository.findeMitId(new AngebotsId(r.getAngebotId()));
              var angebot = angebotOpt.orElse(null);

              String titel = angebot != null ? angebot.getTitel() : null;
              String beschreibung = angebot != null ? angebot.getBeschreibung() : null;

              String von = null, bis = null;
              if (angebot != null && angebot.getZeitfenster() != null) {
                von =
                    angebot.getZeitfenster().von() != null
                        ? angebot.getZeitfenster().von().format(formatter)
                        : null;
                bis =
                    angebot.getZeitfenster().bis() != null
                        ? angebot.getZeitfenster().bis().format(formatter)
                        : null;
              }

              return new com.foodrescue.reservierungsmanagement.infrastructure.web.rest
                  .ReservierungController.GeplanteAbholungResponse(
                  r.getId(),
                  r.getAngebotId(),
                  titel,
                  beschreibung,
                  r.getStatus().name(),
                  r.getAbholcode() != null ? r.getAbholcode().value() : null,
                  von,
                  bis);
            })
        .collect(Collectors.toList());
  }
}
