package com.sapient.creditcardprocessing.exceptions;


public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message){
        super(message);
    }
}
