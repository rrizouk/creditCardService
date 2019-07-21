package com.sapient.creditcardprocessing.data;

import java.math.BigDecimal;
import java.util.Scanner;


public class TestData {

    public static String createCreditCardDetails(String customerName, String cardNumber, BigDecimal limit) {
        String creditCardDetails = loadFromFile("/json-payload/credit-card-details.json")
                .replace("#customerName#", customerName)
                .replace("#cardNumber#", cardNumber)
                .replace("#limit#",limit.toPlainString());
        return creditCardDetails;
    }

    private static String loadFromFile(String path) {
        return new Scanner(TestData.class.getResourceAsStream(path), "UTF-8").useDelimiter("\\A").next();
    }

    public static String createCreditCardTransaction(String customerName,String cardNumber, BigDecimal amount) {
        String creditCardTransactionDetails = loadFromFile("/json-payload/credit-card-transaction-details.json")
                .replace("#customerName#", customerName)
                .replace("#cardNumber#", cardNumber)
                .replace("#amount#", amount.toPlainString());
        return creditCardTransactionDetails;
    }
}
