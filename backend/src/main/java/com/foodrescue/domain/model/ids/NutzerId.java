package com.foodrescue.domain.model.ids;

import com.foodrescue.exceptions.DomainException;

public final class NutzerId {
    private final String value;
    private NutzerId(String value) {
        if (value == null || value.isBlank()){
            throw DomainException.raiseIdInvalid("NutzerId");
        }
        this.value = value;
    }
    public static NutzerId of(String value){ return new NutzerId(value); }
    public String value(){ return value; }
    @Override public String toString(){ return value; }
    @Override public boolean equals(Object o){ return o instanceof NutzerId id && value.equals(id.value); }
    @Override public int hashCode(){ return value.hashCode(); }
}
