package com.foodrescue.application.services;

import com.foodrescue.domain.model.*;
import com.foodrescue.exceptions.DomainException;

import java.util.function.IntSupplier;

public class ReservierungsService {

    private final IntSupplier aktiveReservierungenFuerNutzer; // z.B. via Repository
    private final int maxProNutzer;

    public ReservierungsService(IntSupplier aktiveReservierungenFuerNutzer, int maxProNutzer) {
        this.aktiveReservierungenFuerNutzer = aktiveReservierungenFuerNutzer;
        this.maxProNutzer = maxProNutzer;
    }

    public Reservierung reserviere(Angebot angebot, String userId, Abholcode code) {
        if (aktiveReservierungenFuerNutzer.getAsInt() >= maxProNutzer)
            throw new DomainException("Maximale Anzahl aktiver Reservierungen erreicht");
        return angebot.reservieren(userId, code);
    }
}

