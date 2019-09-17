package com.sweetsystem.me.tranasctionanalyser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.sweetsystem.me.tranasctionanalyser.model.RelativeAccountBalance;
import com.sweetsystem.me.tranasctionanalyser.model.Transaction;
import com.sweetsystem.me.tranasctionanalyser.model.TransactionType;

@RunWith(MockitoJUnitRunner.class)
public class TransactionAnalyserTest {

    private static final String FILENAME = "transactions.csv";

    @Mock
    private TransactionReader transactionReader;

    private TransactionAnalyser analyser;

    private LocalDateTime now;

    @Before
    public void init() throws IOException {
        now = LocalDateTime.now();
        List<Transaction> transactions = Lists.newArrayList(
                transaction("1", "ACT1", "ACT2", now.minusDays(7), new BigDecimal("100.00"), TransactionType.PAYMENT),
                transaction("3", "ACT1", "ACT3", now.minusDays(5), new BigDecimal("10.00"), TransactionType.PAYMENT),
                transaction("4", "ACT2", "ACT1", now.minusDays(4), new BigDecimal("5.00"), TransactionType.PAYMENT),
                transaction("5", "ACT1", "ACT4", now.minusDays(3), new BigDecimal("30.00"), TransactionType.PAYMENT),
                transaction("8", "ACT3", "ACT1", now.minusDays(2), new BigDecimal("102.00"), TransactionType.PAYMENT));

        when(transactionReader.read(FILENAME)).thenReturn(transactions.stream());

        analyser = new TransactionAnalyser(FILENAME, transactionReader);
    }

    @Test
    public void returnsRelativeAccountBalance() {

        RelativeAccountBalance result = analyser.analyse("ACT1", now.minusDays(8), now.minusDays(1));

        assertEquals(5, result.getNumberOfTransactions());
        assertEquals(new BigDecimal("-33.00"), result.getAmount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsExceptionWhenAccountNotInTransactions() {

        analyser.analyse("ACT10", now.minusDays(8), now.minusDays(1));
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
