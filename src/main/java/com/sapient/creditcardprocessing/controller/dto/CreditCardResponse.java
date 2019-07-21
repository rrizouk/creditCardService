package com.sapient.creditcardprocessing.controller.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class CreditCardResponse {

    @JsonProperty
    private String cardNumber;

    private BigDecimal balance;

    @JsonProperty("balance")
    public String getBalance(){
       return "B#".concat(balance.toPlainString());
    }
}
