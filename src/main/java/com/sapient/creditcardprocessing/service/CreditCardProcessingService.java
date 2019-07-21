package com.sapient.creditcardprocessing.service;


import com.sapient.creditcardprocessing.controller.dto.CreditCardDetailsDTO;
import com.sapient.creditcardprocessing.controller.dto.CreditCardResponse;
import com.sapient.creditcardprocessing.domain.CreditCardDetails;
import com.sapient.creditcardprocessing.exceptions.InsufficientBalanceException;
import com.sapient.creditcardprocessing.repository.CreditCardProcessingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreditCardProcessingService {

    private final CreditCardProcessingRepository creditCardProcessingRepository;
    private final CreditCardValidator creditCardValidator;

    @Autowired
    public CreditCardProcessingService(CreditCardProcessingRepository creditCardProcessingRepository, CreditCardValidator creditCardValidator) {
        this.creditCardProcessingRepository = creditCardProcessingRepository;
        this.creditCardValidator = creditCardValidator;
    }

    public void add(CreditCardDetailsDTO creditCardDetailsDTO) {
        creditCardValidator.isValidCreditCardNumber(creditCardDetailsDTO.getCardNumber());
        this.creditCardProcessingRepository.save(buildCreditCardDetails(creditCardDetailsDTO));
    }

    @Transactional
    public CreditCardResponse charge(CreditCardDetailsDTO creditCardDetailsDTO) {
        creditCardValidator.isValidCreditCardNumber(creditCardDetailsDTO.getCardNumber());
        CreditCardDetails creditCardDetails = this.creditCardProcessingRepository.findByCustomerName(creditCardDetailsDTO.getCustomerName());
        debitBalance(creditCardDetails, creditCardDetailsDTO.getAmount());
        this.creditCardProcessingRepository.save(creditCardDetails);
        return new CreditCardResponse(creditCardDetails.getCardNumber(),creditCardDetails.getBalance());
    }

    @Transactional
    public CreditCardResponse credit(CreditCardDetailsDTO creditCardDetailsDTO) {
        creditCardValidator.isValidCreditCardNumber(creditCardDetailsDTO.getCardNumber());
        CreditCardDetails creditCardDetails = this.creditCardProcessingRepository.findByCustomerName(creditCardDetailsDTO.getCustomerName());
        creditBalance(creditCardDetails, creditCardDetailsDTO.getAmount());
        this.creditCardProcessingRepository.save(creditCardDetails);
        return new CreditCardResponse(creditCardDetails.getCardNumber(),creditCardDetails.getBalance());
    }

    private void creditBalance(CreditCardDetails creditCardDetails, BigDecimal amount) {
        BigDecimal updatedBalance = creditCardDetails.getBalance().subtract(amount);
        creditCardDetails.setBalance(updatedBalance);
    }

    private void debitBalance(CreditCardDetails creditCardDetails, BigDecimal amount) {
        BigDecimal updatedBalance = creditCardDetails.getBalance().add(amount);
        if(creditCardDetails.getLimit().compareTo(updatedBalance) < 0){
            throw new InsufficientBalanceException("this amount will exceed your limit: " + creditCardDetails.getLimit());
        }
        creditCardDetails.setBalance(updatedBalance);
    }

    private CreditCardDetails buildCreditCardDetails(CreditCardDetailsDTO creditCardDetailsDTO) {
        CreditCardDetails creditCardDetails = new CreditCardDetails();
        creditCardDetails.setCustomerName(creditCardDetailsDTO.getCustomerName());
        creditCardDetails.setCardNumber(creditCardDetailsDTO.getCardNumber());
        creditCardDetails.setLimit(creditCardDetailsDTO.getLimit());
        creditCardDetails.setBalance(BigDecimal.ZERO);

        return creditCardDetails;
    }
}
