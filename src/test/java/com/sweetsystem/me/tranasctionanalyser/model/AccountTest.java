package com.sweetsystem.me.tranasctionanalyser.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class AccountTest {

    private List<Transaction> transactions;

    private LocalDateTime now;

    @Before
    public void init() {
        now = LocalDateTime.now();
        transactions = Lists.newArrayList(
                transaction("1", "ACT1", "ACT2", now.minusDays(7), new BigDecimal("100.00"), TransactionType.PAYMENT),
                transaction("3", "ACT1", "ACT3", now.minusDays(5), new BigDecimal("10.00"), TransactionType.PAYMENT),
                transaction("4", "ACT2", "ACT1", now.minusDays(4), new BigDecimal("5.00"), TransactionType.PAYMENT),
                transaction("5", "ACT1", "ACT4", now.minusDays(3), new BigDecimal("30.00"), TransactionType.PAYMENT),
                transaction("8", "ACT3", "ACT1", now.minusDays(2), new BigDecimal("102.00"), TransactionType.PAYMENT));
    }

    @Test
    public void allTransactionsIncludedAsInTimeFrame() {

        Account account = new Account("ACT1", transactions);

        RelativeAccountBalance result = account.relativeBalance(now.minusDays(7).minusSeconds(1), now.plusSeconds(1));

        assertEquals(5, result.getNumberOfTransactions());
        assertEquals(new BigDecimal("-33.00"), result.getAmount());
    }

    @Test
    public void excludesTransactionsNotInTimeFrame() {

        Account account = new Account("ACT1", transactions);

        RelativeAccountBalance result = account.relativeBalance(now.minusDays(4).minusSeconds(1), now.minusDays(3).plusSeconds(1));

        assertEquals(2, result.getNumberOfTransactions());
        assertEquals(new BigDecimal("-25.00"), result.getAmount());
    }

    @Test
    public void zeroAmountWhenNoTransactionsInTimeFrame() {

        Account account = new Account("ACT1", transactions);

        RelativeAccountBalance result = account.relativeBalance(now.minusDays(2).plusSeconds(1), now.minusDays(1));

        assertEquals(0, result.getNumberOfTransactions());
        assertEquals(BigDecimal.ZERO, result.getAmount());
    }

    @Test
    public void excludesTransactionsAsReversal() {

        transactions.add(transaction("6", "ACT1", "ACT3", now.minusDays(3), new BigDecimal("10.00"), TransactionType.REVERSAL, "3"));
        Collections.sort(transactions, new Comparator<Transaction>() {

            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o1.getTransactionId().compareTo(o2.getTransactionId());
            }

        });
        Account account = new Account("ACT1", transactions);

        RelativeAccountBalance result = account.relativeBalance(now.minusDays(7).minusSeconds(1), now.plusSeconds(1));

        assertEquals(4, result.getNumberOfTransactions());
        assertEquals(new BigDecimal("-23.00"), result.getAmount());
    }

    @Test
    public void excludesTransactionsAsReversalEvenThoughOutsideWindow() {

        transactions.add(transaction("9", "ACT1", "ACT3", now.minusDays(1), new BigDecimal("10.00"), TransactionType.REVERSAL, "3"));
        Account account = new Account("ACT1", transactions);

        RelativeAccountBalance result = account.relativeBalance(now.minusDays(7).minusSeconds(1), now.minusDays(2).plusSeconds(1));

        assertEquals(4, result.getNumberOfTransactions());
        assertEquals(new BigDecimal("-23.00"), result.getAmount());
    }

    private Transaction transaction(
            String transactionId, String fromAccount, String toAccount, LocalDateTime transactionDate,
            BigDecimal amount, TransactionType transactionType) {
        return transaction(transactionId, fromAccount, toAccount, transactionDate, amount, transactionType, null);
    }

    private Transaction transaction(
            String transactionId, String fromAccount, String toAccount, LocalDateTime transactionDate,
            BigDecimal amount, TransactionType transactionType, String relatedTransaction) {
        return new Transaction(
                transactionId, fromAccount, toAccount, transactionDate, amount, transactionType, relatedTransaction);
    }

}
