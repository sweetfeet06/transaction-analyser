package com.sweetsystem.me.tranasctionanalyser.model;

public enum TransactionType {

    PAYMENT,
    REVERSAL;

    public boolean isReversal() {
        return REVERSAL == this;
    }

}
