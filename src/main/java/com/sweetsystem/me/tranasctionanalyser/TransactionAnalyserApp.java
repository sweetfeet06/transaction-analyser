package com.sweetsystem.me.tranasctionanalyser;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.sweetsystem.me.tranasctionanalyser.model.RelativeAccountBalance;
import com.sweetsystem.me.tranasctionanalyser.model.TransactionFactory;

public class TransactionAnalyserApp {

    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private TransactionAnalyser analyser;


    public TransactionAnalyserApp(TransactionAnalyser analyser) {
        this.analyser = analyser;
    }

    public static void main(String[] args) {
        if (args.length < 1 ) {
            System.out.println("Usage: java -cp target/tranasction-analyser-0.0.1-SNAPSHOT.jar:<dir with csv> com.sweetsystem.me.tranasctionanalyser.TransactionAnalyserApp <csf filename>");
        }


        TransactionFactory factory = new TransactionFactory(DATE_TIME_FORMAT);
        TransactionReader transactionReader = new TransactionReader(factory);
        try {
            new TransactionAnalyserApp(new TransactionAnalyser(args[0], transactionReader)).run();
        }
        catch (IOException e) {
            System.out.println("input file " + args[0] + " not found");
        }

    }

    private void run() {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("accountId: ");
            String accountId = scanner.nextLine();
            System.out.print("from: ");
            String start = scanner.nextLine();
            System.out.print("to: ");
            String end = scanner.nextLine();

            RelativeAccountBalance result = analyser.analyse(accountId, dateTime(start), dateTime(end));
            System.out.println(String.format("Relative balance for the period is: $%s ", result.getAmount().setScale(2, BigDecimal.ROUND_DOWN)));
            System.out.println(String.format("Number of transactions included is: %d", result.getNumberOfTransactions()));
            System.out.println();
        }
    }

    private LocalDateTime dateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

}
