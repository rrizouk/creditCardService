package com.sapient.creditcardprocessing.service;

import com.sapient.creditcardprocessing.exceptions.InvalidCardNumberFormatException;
import com.sapient.creditcardprocessing.exceptions.LuhnCheckIncompatibleException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CreditCardValidatorTest {

    @InjectMocks
    private CreditCardValidator underTest;

    @Test
    public void shouldValidateCreditCardNumber(){
        String validCardNumber = "79927398713";
        underTest.isValidCreditCardNumber(validCardNumber);
    }

    @Test(expected = InvalidCardNumberFormatException.class)
    public void shouldThrowExceptionIfCardNumberIsTooLong(){
        String validCardNumber = "7992739871222222222222222222222222222222222222222222222222222222222222";
        underTest.isValidCreditCardNumber(validCardNumber);
    }

    @Test(expected = InvalidCardNumberFormatException.class)
    public void shouldThrowExceptionIfCardNumberIsNotNumeric(){
        String validCardNumber = "799273987adsbnbff";
        underTest.isValidCreditCardNumber(validCardNumber);
    }

    @Test(expected = LuhnCheckIncompatibleException.class)
    public void shouldThrowExceptionIfCardNumberIsNotLuhn10Compatible(){
        String validCardNumber = "79927398710";
        underTest.isValidCreditCardNumber(validCardNumber);
    }

}