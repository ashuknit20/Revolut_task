package com.revolutmoneyexchange.controller;

import com.revolutmoneyexchange.exceptions.ObjectMisMatchException;
import com.revolutmoneyexchange.model.ApplicationException;
import com.revolutmoneyexchange.model.ExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This class is an Exception Mapper which is responsible for common error output generation.
 * It handles any error fired and transform to appropriate <code>ApplicationException</code> object.
 * This exception object will be returned to the client with the appropriate status.
 */
@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger log = LoggerFactory.getLogger(ThrowableExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        ApplicationException applicationException;
        Response.ResponseBuilder serverError = Response.serverError().type(MediaType.APPLICATION_JSON_TYPE);

        if (exception instanceof WebApplicationException) {
            applicationException = new ApplicationException(ExceptionType.UNEXPECTED_EXCEPTION.name(),
                    computeExceptionMessage(((WebApplicationException) exception).getResponse()),
                    exception.getMessage());
            serverError = serverError.status(((WebApplicationException) exception).getResponse().getStatus());
        } else if (exception instanceof ObjectMisMatchException) {
            ExceptionType type = ((ObjectMisMatchException) exception).getType();

            if (type == ExceptionType.RECORD_NOT_FOUND) {
                serverError = serverError.status(Response.Status.NOT_FOUND);
            }
            if (type == ExceptionType.BAD_REQUEST_OBJECT) {
                serverError = serverError.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
            applicationException = new ApplicationException(type, exception.getMessage());
        } else {
            applicationException = new ApplicationException(ExceptionType.UNEXPECTED_EXCEPTION,
                    exception.getMessage());
        }

        log.error("Uncaught exception", exception);
        return serverError.entity(applicationException).build();
    }

    private static String computeExceptionMessage(Response response) {
        Response.StatusType statusInfo;
        if (response != null) {
            statusInfo = response.getStatusInfo();
        } else {
            statusInfo = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return "HTTP " + statusInfo.getStatusCode() + ' ' + statusInfo.getReasonPhrase();
    }
}