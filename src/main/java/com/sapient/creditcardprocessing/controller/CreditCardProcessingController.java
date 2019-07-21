package com.sapient.creditcardprocessing.controller;


import com.sapient.creditcardprocessing.controller.dto.CreditCardDetailsDTO;
import com.sapient.creditcardprocessing.controller.dto.CreditCardResponse;
import com.sapient.creditcardprocessing.exceptions.InsufficientBalanceException;
import com.sapient.creditcardprocessing.exceptions.InvalidCardNumberFormatException;
import com.sapient.creditcardprocessing.exceptions.LuhnCheckIncompatibleException;
import com.sapient.creditcardprocessing.service.CreditCardProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.util.WebUtils.CONTENT_TYPE_CHARSET_PREFIX;

@Slf4j
@RestController
public class CreditCardProcessingController {

    private static final String APPLICATION_JSON_VALUE_UTF8 = APPLICATION_JSON_VALUE + CONTENT_TYPE_CHARSET_PREFIX + "UTF-8";
    private static final String ERROR_MESSAGE = "system error, please contact administrator";
    private static final String GENERIC_ERROR_MESSAGE = "some generic error";

    private final CreditCardProcessingService creditCardProcessingService;

    @Autowired
    public CreditCardProcessingController(CreditCardProcessingService creditCardProcessingService) {
        this.creditCardProcessingService = creditCardProcessingService;
    }

    @RequestMapping(value = "/credit-card/add", method = POST, consumes = APPLICATION_JSON_VALUE_UTF8)
    @ResponseStatus(CREATED)
    public void addCreditCard(@RequestBody CreditCardDetailsDTO creditCardDetailsDTO){
        log.info("add credit card for customer name {}", creditCardDetailsDTO.getCustomerName());
        creditCardProcessingService.add(creditCardDetailsDTO);
    }

    @RequestMapping(value = "/credit-card/charge", method = POST, consumes = APPLICATION_JSON_VALUE_UTF8)
    @ResponseBody
    public CreditCardResponse chargeCreditCard(@RequestBody CreditCardDetailsDTO creditCardDetailsDTO){
        log.info("charge credit card for customer name {}", creditCardDetailsDTO.getCustomerName());
        return creditCardProcessingService.charge(creditCardDetailsDTO);
    }

    @RequestMapping(value = "/credit-card/credit", method = POST, consumes = APPLICATION_JSON_VALUE_UTF8)
    @ResponseBody
    public CreditCardResponse creditBalanceOfCreditCard(@RequestBody CreditCardDetailsDTO creditCardDetailsDTO){
        log.info("credit balance of card for customer name {}", creditCardDetailsDTO.getCustomerName());
        return creditCardProcessingService.credit(creditCardDetailsDTO);
    }

    @ExceptionHandler
    public ResponseEntity<CreditCardProcessingMessage> handleLuhnCheckIncompatibleException(LuhnCheckIncompatibleException exception) {
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(new CreditCardProcessingMessage("error_internal", ERROR_MESSAGE, ExceptionUtils.getStackTrace(exception)), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<CreditCardProcessingMessage> handleInvalidCardNumberFormatException(InvalidCardNumberFormatException exception){
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(new CreditCardProcessingMessage("error_internal", ERROR_MESSAGE, ExceptionUtils.getStackTrace(exception)), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<CreditCardProcessingMessage> handleInsufficientBalanceException(InsufficientBalanceException exception){
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(new CreditCardProcessingMessage("error_internal", ERROR_MESSAGE, ExceptionUtils.getStackTrace(exception)), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<CreditCardProcessingMessage> handleException(Exception exception){
        log.warn(exception.getMessage(), exception);
        return new ResponseEntity<>(new CreditCardProcessingMessage("error_internal", GENERIC_ERROR_MESSAGE, ExceptionUtils.getStackTrace(exception)), INTERNAL_SERVER_ERROR);
    }




}
