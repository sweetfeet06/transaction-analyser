package com.sweetsystem.me.tranasctionanalyser.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private String transactionId;

    private String fromAccountId;

    private String toAccountId;

    private LocalDateTime createdAt;

    private BigDecimal amount;

    private TransactionType transactionType;

    private String relatedTransaction;

    public Transaction(
            String transactionId, String fromAccountId, String toAccountId, LocalDateTime createdAt,
            BigDecimal amount, TransactionType transactionType, String relatedTransaction) {
        this.transactionId = transactionId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.createdAt = createdAt;
        this.amount = amount;
        this.transactionType = transactionType;
        this.relatedTransaction = relatedTransaction;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getRelatedTransaction() {
        return relatedTransaction;
    }

    public boolean isReversal() {
        return transactionType.isReversal();
    }

}
