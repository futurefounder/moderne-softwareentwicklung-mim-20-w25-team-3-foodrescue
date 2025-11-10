package com.foodrescue.exceptions;

import com.foodrescue.domain.model.DomainError;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
        error = null;
    }

    private final DomainError error;
    public DomainException(DomainError error, String message) {
        super(message);
        this.error = error;
    }

    public DomainError error(){ return error; }

    public static DomainException raise(DomainError error) {
        return new DomainException(error, error.defaultMessage());
    }

    public static DomainException raise(DomainError error, String details) {
        var msg = details == null || details.isBlank()
                ? error.defaultMessage()
                : error.defaultMessage() + ": " + details;
        return new DomainException(error, msg);
    }

    // bequeme Shortcuts für IDs
    public static DomainException raiseIdInvalid(String idName) {
        return raise(DomainError.ID_UNGUELTIG, idName);
    }
}