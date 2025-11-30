package com.foodrescue.userverwaltung.domain.events;

import com.foodrescue.userverwaltung.domain.valueobjects.EmailAdresse;
import com.foodrescue.userverwaltung.domain.valueobjects.Name;
import com.foodrescue.userverwaltung.domain.valueobjects.Rolle;
import com.foodrescue.userverwaltung.domain.valueobjects.UserId;

public class UserRegistriertEvent {

    private final UserId userId;
    private final Name name;
    private final EmailAdresse email;
    private final Rolle rolle;

    public UserRegistriertEvent(UserId userId, Name name, EmailAdresse email, Rolle rolle) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.rolle = rolle;
    }

    public UserId getUserId() {
        return userId;
    }

    public Name getName() {
        return name;
    }

    public EmailAdresse getEmail() {
        return email;
    }

    public Rolle getRolle() {
        return rolle;
    }
}
