package com.revolutmoneyexchange.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;

/**
 * Bank Account entity model. Relates to the database table <code>bank_account</code>. Defines the bank account of
 * individual with <code>ownerName</code>. It has <code>balance</code> in specific money <code>currency</code>. Once
 * there is any PLANNED transferring transaction in the system relates to this Bank Account, the transaction amount is
 * reserved in <code>blockedAmount</code> field
 */
public class BankAccount implements ModelHasId {
    private Long accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private BigDecimal blockedAmount;
    private Currency currency;

    public BankAccount() {
    }

    public BankAccount(String ownerName, BigDecimal balance, BigDecimal blockedAmount, Currency currency) {
        this(new Random().nextLong(), ownerName, balance, blockedAmount, currency);
    }

    public BankAccount(Long accountNumber, String ownerName, BigDecimal balance, BigDecimal blockedAmount, Currency currency) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.blockedAmount = blockedAmount;
        this.currency = currency;
    }

    public BankAccount(Long accountNumber, String ownerName) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBlockedAmount() {
        return blockedAmount;
    }

    public void setBlockedAmount(BigDecimal blockedAmount) {
        this.blockedAmount = blockedAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(getAccountNumber(), that.getAccountNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountNumber());
    }
}
