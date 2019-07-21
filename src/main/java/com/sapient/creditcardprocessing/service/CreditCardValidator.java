package com.sapient.creditcardprocessing.service;

import com.sapient.creditcardprocessing.exceptions.InvalidCardNumberFormatException;
import com.sapient.creditcardprocessing.exceptions.LuhnCheckIncompatibleException;
import org.springframework.stereotype.Component;

@Component
public class CreditCardValidator {

    private static final int MAX_CREDIT_CARD_LENGTH = 19;

    public void isValidCreditCardNumber(String cardNumber) {

      if(cardNumber == null || cardNumber.length() > MAX_CREDIT_CARD_LENGTH || !isNumeric(cardNumber)){
          throw new InvalidCardNumberFormatException("invalid card number : " + cardNumber);
      }
      if(!isLuhn10Compatible(cardNumber)){
          throw new LuhnCheckIncompatibleException("card number is Luhn 10 incompatible : " + cardNumber);
      }

    }

    // this is from wikipedia, i found no need to reinvent the wheel here!!
    private boolean isLuhn10Compatible(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private boolean isNumeric(String cardNumber) {
        try{
            Long.parseLong(cardNumber);
        }catch (NumberFormatException ex){
            return false;
        }
        return true;
    }
}
