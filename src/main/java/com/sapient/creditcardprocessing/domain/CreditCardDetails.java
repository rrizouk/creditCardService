package com.sapient.creditcardprocessing.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "CreditCardDetails")
@Table(name = "CREDIT_CARD_DETAILS")
public class CreditCardDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "CREDIT_LIMIT")
    private BigDecimal limit;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    public void setBalance(BigDecimal balance){
      this.balance = balance;
    }
}
