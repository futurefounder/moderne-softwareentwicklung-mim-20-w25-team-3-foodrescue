package com.foodrescue.abholungsmanagement.application.services;

import com.foodrescue.abholungsmanagement.application.commands.BestaetigeAbholungCommand;
import com.foodrescue.abholungsmanagement.domain.model.Abholung;
import com.foodrescue.abholungsmanagement.infrastructure.repositories.AbholungRepository;
import com.foodrescue.reservierungsmanagement.domain.model.Reservierung;
import com.foodrescue.reservierungsmanagement.infrastructure.repositories.ReservierungRepository;
import com.foodrescue.shared.domain.DomainEvent;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AbholungApplicationService {

    private final AbholungRepository abholungRepository;
    private final ReservierungRepository reservierungRepository;

    public AbholungApplicationService(AbholungRepository abholungRepository, ReservierungRepository reservierungRepository) {
        this.abholungRepository = abholungRepository;
        this.reservierungRepository = reservierungRepository;
    }

    public void bestaetigeAbholung(BestaetigeAbholungCommand command) {
        Optional<Reservierung> optionalReservierung = reservierungRepository.findeMitId(command.getReservierungsId().value());
        Reservierung reservierung = optionalReservierung.orElseThrow(() -> new IllegalArgumentException("Reservierung nicht gefunden"));

        Abholung abholung = new Abholung(java.util.UUID.randomUUID().toString(), reservierung.getId(), reservierung.getAbholcode());
        List<DomainEvent> events = abholung.bestaetigen(command.getCode());
        abholungRepository.speichern(abholung);
        reservierung.bestaetigeAbholung(command.getCode());
        reservierungRepository.speichern(reservierung);
        // Publish events
    }
}