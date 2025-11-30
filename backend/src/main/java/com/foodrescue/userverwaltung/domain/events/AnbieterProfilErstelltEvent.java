package com.foodrescue.userverwaltung.domain.events;

import com.foodrescue.userverwaltung.domain.valueobjects.AnbieterProfilId;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;

public class AnbieterProfilErstelltEvent {

    private final AnbieterProfilId anbieterProfilId;
    private final UserId userId;

    public AnbieterProfilErstelltEvent(AnbieterProfilId anbieterProfilId, UserId userId) {
        this.anbieterProfilId = anbieterProfilId;
        this.userId = userId;
    }

    public AnbieterProfilId getAnbieterProfilId() {
        return anbieterProfilId;
    }

    public UserId getUserId() {
        return userId;
    }
}
