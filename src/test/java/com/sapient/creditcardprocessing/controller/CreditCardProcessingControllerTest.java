package com.sapient.creditcardprocessing.controller;



import com.sapient.creditcardprocessing.controller.dto.CreditCardDetailsDTO;
import com.sapient.creditcardprocessing.controller.dto.CreditCardResponse;
import com.sapient.creditcardprocessing.data.TestData;
import com.sapient.creditcardprocessing.exceptions.InsufficientBalanceException;
import com.sapient.creditcardprocessing.exceptions.InvalidCardNumberFormatException;
import com.sapient.creditcardprocessing.exceptions.LuhnCheckIncompatibleException;
import com.sapient.creditcardprocessing.service.CreditCardProcessingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;

import static com.sapient.creditcardprocessing.data.TestData.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class CreditCardProcessingControllerTest {


    MockMvc mockMvc;

    private static final String ADD_CARD_URL = "/credit-card/add";
    private static final String CHARGE_CARD_URL = "/credit-card/charge";
    private static final String CREDIT_CARD_URL = "/credit-card/credit";

    @InjectMocks
    private CreditCardProcessingController underTest;

    @Mock
    private CreditCardProcessingService creditCardProcessingService;


    @Before
    public void setUp() throws Exception {
       this.mockMvc = standaloneSetup(underTest).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void shouldAddCreditCard() throws Exception {

        String customerName = "Ryad";
        String cardNumber = "1234566788";
        BigDecimal limit = BigDecimal.valueOf(1000);

        String creditCardDetails = createCreditCardDetails(customerName,cardNumber, limit);

        mockMvc.perform(post(ADD_CARD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditCardDetails))
                .andExpect(status().isCreated());

    }

   @Test
   public void shouldChargeCreditCard() throws Exception {
       String customerName = "Ryad";
       BigDecimal amount = BigDecimal.valueOf(499.99);
       BigDecimal balance = BigDecimal.valueOf(500.01);
       String formattedBalance = "B#500.01";
       String cardNumber = "1234566788";

       String creditCardTransaction = TestData.createCreditCardTransaction(customerName, cardNumber, amount);

       given(creditCardProcessingService.charge(any(CreditCardDetailsDTO.class))).willReturn(dummyResponse(cardNumber,balance));

       mockMvc.perform(post(CHARGE_CARD_URL)
               .contentType(MediaType.APPLICATION_JSON)
               .content(creditCardTransaction))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.cardNumber").value(cardNumber))
               .andExpect(jsonPath("$.balance").value(formattedBalance));
   }

    @Test
    public void shouldCreditBalanceOfCreditCard() throws Exception {
        String customerName = "Ryad";
        BigDecimal amount = BigDecimal.valueOf(44499.99);
        BigDecimal balance = BigDecimal.valueOf(500.01);
        String formattedBalance = "B#500.01";
        String cardNumber = "1234566788";

        String creditCardTransaction = TestData.createCreditCardTransaction(customerName,cardNumber, amount);

        given(creditCardProcessingService.credit(any(CreditCardDetailsDTO.class))).willReturn(dummyResponse(cardNumber,balance));

        mockMvc.perform(post(CREDIT_CARD_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditCardTransaction))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(cardNumber))
                .andExpect(jsonPath("$.balance").value(formattedBalance));
    }

    @Test
    public void shouldNotChargeCreditCardWhenAmountExceedsLimit() throws Exception {
        String customerName = "Ryad";
        String cardNumber = "1234566788";

        BigDecimal amount = BigDecimal.valueOf(499999.99);
        String creditCardTransaction = TestData.createCreditCardTransaction(customerName, cardNumber, amount);

        doThrow(InsufficientBalanceException.class).when(creditCardProcessingService).charge(any(CreditCardDetailsDTO.class));

        mockMvc.perform(post(CHARGE_CARD_URL)
                .contentType(MediaType.APPLICATION_JSON).content(creditCardTransaction)).andExpect(status().isInternalServerError());
    }



    @Test
    public void shouldReturnErrorWhenCardIsLuhnIncompatible() throws Exception {
        String customerName = "Ryad";
        String cardNumber = "1234566788";
        BigDecimal limit = BigDecimal.valueOf(1000);

        String creditCardDetails = createCreditCardDetails(customerName, cardNumber, limit);
        doThrow(LuhnCheckIncompatibleException.class).when(creditCardProcessingService).add(any(CreditCardDetailsDTO.class));

        mockMvc.perform(post(ADD_CARD_URL)
                .contentType(MediaType.APPLICATION_JSON).content(creditCardDetails)).andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnErrorWhenCardIsInvalid() throws Exception {
        String customerName = "Ryad";
        String cardNumber = "qwwrteyuureur";
        BigDecimal limit = BigDecimal.valueOf(1000);

        String creditCardDetails = createCreditCardDetails(customerName,cardNumber, limit);
        doThrow(InvalidCardNumberFormatException.class).when(creditCardProcessingService).add(any(CreditCardDetailsDTO.class));

        mockMvc.perform(post(ADD_CARD_URL)
                .contentType(MediaType.APPLICATION_JSON).content(creditCardDetails)).andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnErrorWhenCardIsInvalidForCharge() throws Exception {
        String customerName = "Ryad";
        String cardNumber = "qwwrteyuureur";
        BigDecimal limit = BigDecimal.valueOf(1000);

        String creditCardDetails = createCreditCardDetails(customerName,cardNumber, limit);
        doThrow(InvalidCardNumberFormatException.class).when(creditCardProcessingService).credit(any(CreditCardDetailsDTO.class));

        mockMvc.perform(post(CREDIT_CARD_URL)
                .contentType(MediaType.APPLICATION_JSON).content(creditCardDetails)).andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldReturnErrorWhenCardIsInvalidForCredit() throws Exception {
        String customerName = "Ryad";
        String cardNumber = "qwwrteyuureur";
        BigDecimal limit = BigDecimal.valueOf(1000);

        String creditCardDetails = createCreditCardDetails(customerName,cardNumber, limit);
        doThrow(InvalidCardNumberFormatException.class).when(creditCardProcessingService).charge(any(CreditCardDetailsDTO.class));

        mockMvc.perform(post(CHARGE_CARD_URL)
                .contentType(MediaType.APPLICATION_JSON).content(creditCardDetails)).andExpect(status().isInternalServerError());
    }

    private CreditCardResponse dummyResponse(String cardNumber, BigDecimal balance) {
        return new CreditCardResponse(cardNumber,balance);
    }



}