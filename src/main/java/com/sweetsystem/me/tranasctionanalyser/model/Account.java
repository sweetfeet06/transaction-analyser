package com.sweetsystem.me.tranasctionanalyser.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

public class Account {

    private String accountId;

    private List<Transaction> transactions = Lists.newArrayList();

    public Account(String accountNumber, List<Transaction> transactions) {
        this(accountNumber);
        this.transactions.addAll(transactions);
    }

    public Account(String accountNumber) {
        this.accountId = accountNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public RelativeAccountBalance relativeBalance(LocalDateTime start, LocalDateTime end) {

        List<Transaction> includedTransactions = includedTransactions(start, end);

        return new RelativeAccountBalance(relativeBalance(includedTransactions), includedTransactions.size());
    }

    public void withTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    private BigDecimal relativeBalance(List<Transaction> includedTransactions) {
        return includedTransactions.stream()
            .map(transaction -> transactionAmount(transaction))
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    private List<Transaction> includedTransactions(LocalDateTime start, LocalDateTime end) {

        Set<String> reversedTransactionIds = reversedTransactionIds();

        return transactions.stream()
            .filter(transaction -> !transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt().isAfter(end))
            .filter(transaction -> !reversedTransactionIds.contains(transaction.getTransactionId()))
            .filter(transaction -> !transaction.isReversal())
            .collect(Collectors.toList());
    }

    private Set<String> reversedTransactionIds() {
        return transactions.stream()
                .filter(transaction -> transaction.isReversal())
                .map(transaction -> transaction.getRelatedTransaction())
                .collect(Collectors.toSet());
    }

    private BigDecimal transactionAmount(Transaction transaction) {
        if (accountId.equals(transaction.getToAccountId())) {
            return transaction.getAmount();
        }
        return transaction.getAmount().negate();
    }

}
