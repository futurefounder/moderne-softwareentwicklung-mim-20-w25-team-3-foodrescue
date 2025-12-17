package com.foodrescue.angebotsmanagement.application.commands;

import com.foodrescue.angebotsmanagement.domain.valueobjects.AngebotsId;

public class VeroeffentlicheAngebotCommand {
    private final AngebotsId angebotId;

    public VeroeffentlicheAngebotCommand(AngebotsId angebotId) {
        this.angebotId = angebotId;
    }

    public AngebotsId getAngebotId() {
        return angebotId;
    }
}