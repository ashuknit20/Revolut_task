package com.revolutmoneyexchange.service;

import com.revolutmoneyexchange.dto.BankAccountDto;
import com.revolutmoneyexchange.exceptions.ObjectMisMatchException;
import com.revolutmoneyexchange.model.BankAccount;

import java.util.Collection;

public class BankAccountService {
    private static final BankAccountService bas = new BankAccountService();

    public static BankAccountService getInstance() {
        return bas;
    }

    public Collection<BankAccount> getAllBankAccounts() {
        return BankAccountDto.getInstance().getAllBankAccounts();
    }

    public BankAccount getBankAccountById(Long id) {
        return BankAccountDto.getInstance().getBankAccountById(id);
    }

    public void updateBankAccount(BankAccount bankAccount) throws ObjectMisMatchException {
        BankAccountDto.getInstance().updateBankAccountSafe(bankAccount);
    }

    public BankAccount createBankAccount(BankAccount bankAccount) throws ObjectMisMatchException {
        return BankAccountDto.getInstance().createBankAccount(bankAccount);
    }
}
