package com.sapient.creditcardprocessing.controller.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class CreditCardDetailsDTO {

    @JsonProperty
    private String customerName;

    @JsonProperty
    private String cardNumber;

    @JsonProperty
    private BigDecimal limit;

    @JsonProperty
    private BigDecimal amount;
}
