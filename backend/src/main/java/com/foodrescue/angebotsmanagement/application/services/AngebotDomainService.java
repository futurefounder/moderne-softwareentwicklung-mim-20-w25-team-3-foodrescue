package com.foodrescue.angebotsmanagement.application.services;

import com.foodrescue.abholungsmanagement.domain.model.AbholZeitfenster;
import com.foodrescue.angebotsmanagement.domain.model.Angebot;
import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class AngebotDomainService {

    public Angebot erstelleAngebot(AngebotsId id, UserId anbieterId, String titel, String beschreibung, Set<String> tags, AbholZeitfenster zeitfenster) {
        return Angebot.erstelle(id, anbieterId, titel, beschreibung, tags, zeitfenster);
    }
}