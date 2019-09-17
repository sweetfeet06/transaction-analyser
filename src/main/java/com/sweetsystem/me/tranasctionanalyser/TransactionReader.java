package com.sweetsystem.me.tranasctionanalyser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.sweetsystem.me.tranasctionanalyser.model.Transaction;
import com.sweetsystem.me.tranasctionanalyser.model.TransactionFactory;

public class TransactionReader {

    private TransactionFactory factory;

    public TransactionReader(TransactionFactory factory) {
        this.factory = factory;
    }

    public Stream<Transaction> read(String filename) throws IOException {
        return read(Files.lines(Paths.get(filename)));
    }

    private Stream<Transaction> read(Stream<String> lines) {
        return lines
                .filter(line -> !line.startsWith("transactionId"))
                .map(input -> factory.create(input));
    }

}
