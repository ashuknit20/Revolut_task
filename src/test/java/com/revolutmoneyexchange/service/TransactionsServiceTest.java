package com.revolutmoneyexchange.service;

import com.revolutmoneyexchange.dto.BankAccountDto;
import com.revolutmoneyexchange.dto.TransactionDto;
import com.revolutmoneyexchange.exceptions.ObjectMisMatchException;
import com.revolutmoneyexchange.model.Currency;
import com.revolutmoneyexchange.model.Transaction;
import com.revolutmoneyexchange.model.TransactionStatus;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertArrayEquals;

public class TransactionsServiceTest {
    private static final TransactionsService staticTransactionService = TransactionsService.getInstance(
            new ConstantMoneyExchangeService()
    );

    @Test
    public void testAllTransactionsRetrieval(){
        TransactionDto transactionDto = mock(TransactionDto.class);
        TransactionsService transactionsService = new TransactionsService(transactionDto);

        Collection<Transaction> testList = Arrays.asList(
                new Transaction(
                        BankAccountDto.DHARM_BANK_ACCOUNT_ID,
                        BankAccountDto.MIKE_BANK_ACCOUNT_ID,
                        BigDecimal.ZERO,
                        Currency.EUR),
                new Transaction(
                        BankAccountDto.MIKE_BANK_ACCOUNT_ID,
                        BankAccountDto.TEST_USER_BANK_ACCOUNT_ID,
                        BigDecimal.ZERO,
                        Currency.EUR)
        );

        when(transactionDto.getAllTransactions()).thenReturn(testList);

        Collection<Transaction> transactions = transactionsService.getAllTransactions();

        assertNotNull(transactions);
        assertArrayEquals(testList.toArray(), transactions.toArray());
    }

    /**
     * //Test null from account
     *
     * @throws ObjectMisMatchException
     */
    @Test(expectedExceptions= ObjectMisMatchException.class)
    public void testCreateTransactionWithNullFrom() throws ObjectMisMatchException {
        staticTransactionService.createTransaction(new Transaction(
                null, 2L, BigDecimal.TEN, Currency.INR
        ));
    }

    /**
     * Test null to account
     *
     * @throws ObjectMisMatchException
     */
    @Test(expectedExceptions= ObjectMisMatchException.class)
    public void testCreateTransactionWithNullTo() throws ObjectMisMatchException {
        staticTransactionService.createTransaction(new Transaction(
                1L, null, BigDecimal.TEN, Currency.INR
        ));
    }

    /**
     * Test transaction creation with the same accounts
     *
     * @throws ObjectMisMatchException
     */
    @Test(expectedExceptions= ObjectMisMatchException.class)
    public void testCreateTransactionWithSameAccounts() throws ObjectMisMatchException {
        staticTransactionService.createTransaction(new Transaction(
                BankAccountDto.DHARM_BANK_ACCOUNT_ID,
                BankAccountDto.DHARM_BANK_ACCOUNT_ID,
                BigDecimal.TEN,
                Currency.INR
        ));
    }

    /**
     * Test transaction creation with zero amount
     *
     * @throws ObjectMisMatchException
     */
    @Test(expectedExceptions= ObjectMisMatchException.class)
    public void testCreateTransactionWithZeroAmount() throws ObjectMisMatchException {
        staticTransactionService.createTransaction(new Transaction(
                BankAccountDto.DHARM_BANK_ACCOUNT_ID,
                BankAccountDto.MIKE_BANK_ACCOUNT_ID,
                BigDecimal.ZERO,
                Currency.INR
        ));
    }

    /**
     * Testing of Transaction creation and execution. Once transaction has been created
     * the scheduled job will execute it.
     *
     * @throws ObjectMisMatchException
     */
    @Test
    public void testCreateTransaction() throws ObjectMisMatchException {
        Long TRANSACTION_ID = 123L;

        TransactionDto transactionDto = mock(TransactionDto.class);

        Transaction transaction = new Transaction(
                BankAccountDto.DHARM_BANK_ACCOUNT_ID,
                BankAccountDto.MIKE_BANK_ACCOUNT_ID,
                BigDecimal.TEN,
                Currency.INR
        );
        transaction.setAccountNumber(TRANSACTION_ID);

        when(transactionDto.createTransaction(any())).thenReturn(transaction);

        when(transactionDto.getAllTransactionIdsByStatus(any())).thenReturn(
                Collections.singletonList(transaction.getAccountNumber())
        );

        doAnswer(invocation -> {
            transaction.setStatus(TransactionStatus.SUCCEED);
            return null;
        }).when(transactionDto).executeTransaction(anyLong());

        TransactionsService transactionsService = new TransactionsService(transactionDto);
        Transaction createdTransaction = transactionsService.createTransaction(transaction);

        assertEquals(createdTransaction, transaction);
        assertEquals(createdTransaction.getStatus(), TransactionStatus.PLANNED);

        transactionsService.executeTransactions();

        assertEquals(transaction.getStatus(), TransactionStatus.SUCCEED);
    }
}
