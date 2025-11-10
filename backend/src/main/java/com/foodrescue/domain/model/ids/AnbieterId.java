package com.foodrescue.domain.model.ids;

import com.foodrescue.exceptions.DomainException;

public final class AnbieterId {
    private final String value;
    private AnbieterId(String value) {
        if (value == null || value.isBlank()){
            throw DomainException.raiseIdInvalid("AnbieterId");
        }
        this.value = value;
    }
    public static AnbieterId of(String value){ return new AnbieterId(value); }
    public String value(){ return value; }
    @Override public String toString(){ return value; }
    @Override public boolean equals(Object o){ return o instanceof AnbieterId id && value.equals(id.value); }
    @Override public int hashCode(){ return value.hashCode(); }
}

