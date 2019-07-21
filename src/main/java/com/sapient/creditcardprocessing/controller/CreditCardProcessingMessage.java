package com.sapient.creditcardprocessing.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@JsonInclude
@Getter
public class CreditCardProcessingMessage {

    private final String code;
    private final String message;
    private final String trace;

    public CreditCardProcessingMessage(String code, String message, String trace) {
        this.code = code;
        this.message = message;
        this.trace = trace;
    }
}
