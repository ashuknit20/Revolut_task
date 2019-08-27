package com.revolutmoneyexchange.exceptions;

import com.revolutmoneyexchange.model.ExceptionType;

/**
 * The exception which is thrown once some validation or data consistency error detected. It has additional
 * field {@link ExceptionType} which specify additional nature of the exception
 */
public class ObjectMisMatchException extends Exception {
    private ExceptionType type;

    public ObjectMisMatchException(ExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        type = exceptionType;
    }

    public ObjectMisMatchException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        type = exceptionType;
    }

    public ObjectMisMatchException(ExceptionType exceptionType, String message) {
        super(exceptionType.getMessage() + ": " + message);
        type = exceptionType;
    }

    public ExceptionType getType() {
        return type;
    }
}
