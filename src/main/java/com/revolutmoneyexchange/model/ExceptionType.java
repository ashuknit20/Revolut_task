package com.revolutmoneyexchange.model;

import com.revolutmoneyexchange.exceptions.ObjectMisMatchException;

/**
 * Used mostly in the {@link ObjectMisMatchException} to
 * specify the particular type of the exception
 */
public enum ExceptionType {
    BAD_REQUEST_OBJECT("The entity passed has been malformed"),
    RECORD_NOT_FOUND("The entity with provided ID has not been found"),
    CANNOT_GENERATE_ID("The system could not generate ID for this entity. Creation is failed."),
    UNEXPECTED_EXCEPTION("Unexpected exception");

    private String message;

    ExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {return message;}

    @Override
    public String toString() {
        return message;
    }
}
