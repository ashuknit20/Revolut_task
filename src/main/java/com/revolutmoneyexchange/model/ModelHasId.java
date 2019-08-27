package com.revolutmoneyexchange.model;

/**
 * Defines class which could have ID assigned
 */
public interface ModelHasId {
    Long getAccountNumber();

    void setAccountNumber(Long accountNumber);
}
