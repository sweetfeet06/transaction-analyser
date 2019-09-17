package com.sweetsystem.me.tranasctionanalyser.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionFactory {

    private String dateTimeFormat;

    public TransactionFactory(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public Transaction create(String transactionInformation) {

        String[] parts = transactionInformation.split(",");
        String relatedTransactionId = null;
        if (parts.length > 6) {
            relatedTransactionId = parts[6].trim();
        }

        return new Transaction(
                parts[0].trim(), parts[1].trim(), parts[2].trim(), dateTime(parts[3].trim()), new BigDecimal(parts[4].trim()),
                TransactionType.valueOf(parts[5].trim()), relatedTransactionId);
    }

    private LocalDateTime dateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

}
