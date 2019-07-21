package com.sapient.creditcardprocessing.exceptions;

public class LuhnCheckIncompatibleException extends RuntimeException {
    public LuhnCheckIncompatibleException(String message) {
        super(message);
    }
}
