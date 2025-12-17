package com.foodrescue.abholungsmanagement.application.commands;

import com.foodrescue.abholungsmanagement.domain.model.Abholcode;
import com.foodrescue.reservierungsmanagement.domain.valueobjects.ReservierungsId;

public class BestaetigeAbholungCommand {
    private final ReservierungsId reservierungsId;
    private final Abholcode code;

    public BestaetigeAbholungCommand(ReservierungsId reservierungsId, Abholcode code) {
        this.reservierungsId = reservierungsId;
        this.code = code;
    }

    public ReservierungsId getReservierungsId() {
        return reservierungsId;
    }

    public Abholcode getCode() {
        return code;
    }
}
