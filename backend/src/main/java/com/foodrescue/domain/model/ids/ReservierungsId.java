package com.foodrescue.domain.model.ids;

import com.foodrescue.exceptions.DomainException;

public final class ReservierungsId {
    private final String value;
    private ReservierungsId(String value) {
        if (value == null || value.isBlank()){
            throw DomainException.raiseIdInvalid("ReservierungsId");
        }
        this.value = value;
    }
    public static ReservierungsId of(String value){ return new ReservierungsId(value); }
    public String value(){ return value; }
    @Override public String toString(){ return value; }
    @Override public boolean equals(Object o){ return o instanceof ReservierungsId id && value.equals(id.value); }
    @Override public int hashCode(){ return value.hashCode(); }
}
