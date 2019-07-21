package com.sapient.creditcardprocessing.exceptions;


public class InvalidCardNumberFormatException extends RuntimeException {
    public InvalidCardNumberFormatException(String message) {
        super(message);
    }
}
