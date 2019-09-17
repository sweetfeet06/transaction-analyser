package com.sweetsystem.me.tranasctionanalyser.model;

import java.math.BigDecimal;

public class RelativeAccountBalance {

    private BigDecimal amount;

    private int numberOfTransactions;


    public RelativeAccountBalance(BigDecimal amount, int numberOfTransactions) {
        super();
        this.amount = amount;
        this.numberOfTransactions = numberOfTransactions;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

}
