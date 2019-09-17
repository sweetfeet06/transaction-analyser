package com.sweetsystem.me.tranasctionanalyser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import com.sweetsystem.me.tranasctionanalyser.model.Account;
import com.sweetsystem.me.tranasctionanalyser.model.RelativeAccountBalance;
import com.sweetsystem.me.tranasctionanalyser.model.Transaction;

public class TransactionAnalyser {

    private TransactionReader transactionReader;

    private Map<String, Account> accounts = Maps.newHashMap();

    public TransactionAnalyser(String filename, TransactionReader transactionReader) throws IOException {
        this.transactionReader = transactionReader;
        initialiseAccount(transactionReader.read(filename));
    }

    public RelativeAccountBalance analyse(String accountId, LocalDateTime start, LocalDateTime end) {
        if (!accounts.containsKey(accountId)) {
            throw new IllegalArgumentException("Account not found");
        }

        return accounts.get(accountId).relativeBalance(start, end);
    }

    private void initialiseAccount(Stream<Transaction> transactions) {
        transactions.forEach(transaction -> addToAccount(transaction));

    }

    private void addToAccount(Transaction transaction) {
        addToSourceAccount(transaction);
        addToTargetAccount(transaction);
    }

    private void addToTargetAccount(Transaction transaction) {
        addToAccount(transaction.getToAccountId(), transaction);
    }

    private void addToSourceAccount(Transaction transaction) {
        addToAccount(transaction.getFromAccountId(), transaction);
    }

    private void addToAccount(String accountId, Transaction transaction) {
        account(accountId).withTransaction(transaction);
    }

    private Account account(String accountId) {
        if (!accounts.containsKey(accountId)) {
            accounts.put(accountId, new Account(accountId));
        }
        return accounts.get(accountId);
    }

}
