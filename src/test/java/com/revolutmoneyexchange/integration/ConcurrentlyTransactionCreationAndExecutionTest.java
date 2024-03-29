package com.revolutmoneyexchange.integration;

import com.revolutmoneyexchange.exceptions.ObjectMisMatchException;
import com.revolutmoneyexchange.model.BankAccount;
import com.revolutmoneyexchange.model.Currency;
import com.revolutmoneyexchange.model.Transaction;
import com.revolutmoneyexchange.service.BankAccountService;
import com.revolutmoneyexchange.service.ConstantMoneyExchangeService;
import com.revolutmoneyexchange.service.TransactionsService;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;

public class ConcurrentlyTransactionCreationAndExecutionTest {
    private TransactionsService transactionsService = TransactionsService.getInstance(new ConstantMoneyExchangeService());
    private BankAccountService bankAccountService = BankAccountService.getInstance();

    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(1000L);
    private static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.ONE;
    private static final int INVOCATION_COUNT = 1000;

    private Long fromBankAccountId;
    private Long toBankAccountId;
    private AtomicInteger invocationsDone = new AtomicInteger(0);

    @BeforeClass
    public void initData() throws ObjectMisMatchException {
        BankAccount fromBankAccount = new BankAccount(
                "New Bank Account 1",
                INITIAL_BALANCE,
                BigDecimal.ZERO,
                Currency.EUR
        );

        BankAccount toBankAccount = new BankAccount(
                "New Bank Account 2",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                Currency.USD
        );

        fromBankAccountId = bankAccountService.createBankAccount(fromBankAccount).getAccountNumber();
        toBankAccountId = bankAccountService.createBankAccount(toBankAccount).getAccountNumber();
    }

    @Test(threadPoolSize = 100, invocationCount = INVOCATION_COUNT)
    public void testConcurrentTransactionCreation() throws ObjectMisMatchException {
        int currentTestNumber = invocationsDone.addAndGet(1);

        Transaction transaction = new Transaction(
                fromBankAccountId,
                toBankAccountId,
                TRANSACTION_AMOUNT,
                Currency.EUR
        );

        transactionsService.createTransaction(transaction);

        if (currentTestNumber % 5 == 0) {
            transactionsService.executeTransactions();
        }
    }

    @AfterClass
    public void checkResults() {
        transactionsService.executeTransactions();
        BankAccount fromBankAccount = bankAccountService.getBankAccountById(fromBankAccountId);
        assertThat(fromBankAccount.getBalance(),
                Matchers.comparesEqualTo(
                        INITIAL_BALANCE.subtract(
                                TRANSACTION_AMOUNT.multiply(BigDecimal.valueOf(INVOCATION_COUNT)))
                )
        );
        assertThat(fromBankAccount.getBlockedAmount(), Matchers.comparesEqualTo(BigDecimal.ZERO));
    }
}
