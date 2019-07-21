package com.sapient.creditcardprocessing.service;

import com.sapient.creditcardprocessing.controller.dto.CreditCardDetailsDTO;
import com.sapient.creditcardprocessing.controller.dto.CreditCardResponse;
import com.sapient.creditcardprocessing.domain.CreditCardDetails;
import com.sapient.creditcardprocessing.exceptions.InsufficientBalanceException;
import com.sapient.creditcardprocessing.repository.CreditCardProcessingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static java.math.BigDecimal.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CreditCardProcessingServiceTest {

    @InjectMocks
    private CreditCardProcessingService underTest;

    @Mock
    private CreditCardProcessingRepository creditCardProcessingRepository;

    @Mock
    private CreditCardValidator creditCardValidator;

    @Captor
    private ArgumentCaptor<CreditCardDetails> creditCardDetailsCapture;



    @Test
    public void shouldAddCreditCardDetails(){
        CreditCardDetailsDTO creditCardDetailsDTO = mock(CreditCardDetailsDTO.class);
        String customerName = "Ryad";
        String cardNumber = "1234567890";
        BigDecimal limit = valueOf(500);

        given(creditCardDetailsDTO.getCustomerName()).willReturn(customerName);
        given(creditCardDetailsDTO.getCardNumber()).willReturn(cardNumber);
        given(creditCardDetailsDTO.getLimit()).willReturn(limit);

        underTest.add(creditCardDetailsDTO);

        verify(creditCardProcessingRepository).save(creditCardDetailsCapture.capture());

        assertThat(creditCardDetailsCapture.getValue().getCustomerName(), is(customerName));
        assertThat(creditCardDetailsCapture.getValue().getCardNumber(),is(cardNumber));
        assertThat(creditCardDetailsCapture.getValue().getLimit(),is(limit));
        assertThat(creditCardDetailsCapture.getValue().getBalance(),is(ZERO));

    }

    @Test
    public void shouldChargeCreditCard(){
        CreditCardDetailsDTO creditCardDetailsDTO = mock(CreditCardDetailsDTO.class);
        CreditCardDetails creditCardDetails = mock(CreditCardDetails.class);
        String customerName = "Ryad";
        String cardNumber = "1234567890";
        BigDecimal limit = valueOf(500);
        BigDecimal amount = valueOf(99.99);
        BigDecimal balance = BigDecimal.ZERO;

        given(creditCardDetailsDTO.getAmount()).willReturn(amount);
        given(creditCardDetailsDTO.getCustomerName()).willReturn(customerName);

        given(creditCardDetails.getCardNumber()).willReturn(cardNumber);
        given(creditCardDetails.getLimit()).willReturn(limit);
        given(creditCardDetails.getBalance()).willReturn(balance);
        given(creditCardProcessingRepository.findByCustomerName(customerName)).willReturn(creditCardDetails);

        CreditCardResponse response = underTest.charge(creditCardDetailsDTO);

        assertThat(response.getCardNumber(),is(cardNumber));
        verify(creditCardDetails).setBalance(amount);
    }

    @Test
    public void shouldCreditBalanceForCreditCard(){
        CreditCardDetailsDTO creditCardDetailsDTO = mock(CreditCardDetailsDTO.class);
        CreditCardDetails creditCardDetails = mock(CreditCardDetails.class);
        String customerName = "Ryad";
        String cardNumber = "1234567890";
        BigDecimal amount = valueOf(999.99);
        BigDecimal balance = BigDecimal.ZERO;

        given(creditCardDetailsDTO.getAmount()).willReturn(amount);
        given(creditCardDetailsDTO.getCustomerName()).willReturn(customerName);

        given(creditCardDetails.getCardNumber()).willReturn(cardNumber);
        given(creditCardDetails.getBalance()).willReturn(balance);
        given(creditCardProcessingRepository.findByCustomerName(customerName)).willReturn(creditCardDetails);

        CreditCardResponse response = underTest.credit(creditCardDetailsDTO);

        assertThat(response.getCardNumber(),is(cardNumber));
        verify(creditCardDetails).setBalance(amount.negate());
    }

    @Test(expected = InsufficientBalanceException.class)
    public void shouldThrowExceptionWhenBalanceExceedsLimit(){
        CreditCardDetailsDTO creditCardDetailsDTO = mock(CreditCardDetailsDTO.class);
        CreditCardDetails creditCardDetails = mock(CreditCardDetails.class);
        String customerName = "Ryad";
        BigDecimal limit = valueOf(500);
        BigDecimal amount = valueOf(599.99);
        BigDecimal balance = BigDecimal.ZERO;

        given(creditCardDetailsDTO.getAmount()).willReturn(amount);
        given(creditCardDetailsDTO.getCustomerName()).willReturn(customerName);

       given(creditCardDetails.getLimit()).willReturn(limit);
        given(creditCardDetails.getBalance()).willReturn(balance);
        given(creditCardProcessingRepository.findByCustomerName(customerName)).willReturn(creditCardDetails);

        underTest.charge(creditCardDetailsDTO);
    }

}