package com.ironhack.midtermproject.controller.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.controller.dto.AccountDTO;
import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.midtermproject.controller.dto.TransferDTO;
import com.ironhack.midtermproject.dao.accounts.CheckingAccount;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.dao.roles.Address;
import com.ironhack.midtermproject.dao.roles.Admin;
import com.ironhack.midtermproject.dao.roles.ThirdParty;
import com.ironhack.midtermproject.repository.*;
import com.ironhack.midtermproject.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();



    private AccountHolder user1;
    private AccountHolder user2;
    private Admin admin1;
    private ThirdParty thirdParty1;


    private CheckingAccount account1;
    private CheckingAccount account2;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Address address1 = new Address("Test Street","Test Postcode");
        addressRepository.save(address1);
        user1 = new AccountHolder("Test Customer1", LocalDate.of(1980,2,11),address1);
        accountHolderRepository.save(user1);
        account1 = new CheckingAccount(new Money(new BigDecimal(3500)),user1,"SomeSecretKey");
        checkingAccountRepository.save(account1);

        Address address2 = new Address("Test Road","Test Post2");
        addressRepository.save(address2);
        user2 = new AccountHolder("Test Customer2", LocalDate.of(2014,2,11),address2);
        accountHolderRepository.save(user2);
        account2 = new CheckingAccount(new Money(new BigDecimal(2500)),user2,"SomeSecretKey");
        checkingAccountRepository.save(account2);

        admin1 = new Admin("Test Admin");
        adminRepository.save(admin1);

        thirdParty1 = new ThirdParty("Test Third Party","T3stTh4rd");
        thirdPartyRepository.save(thirdParty1);



    }

    @AfterEach
    void tearDown() {
        transactionsRepository.deleteAll();
        creditCardRepository.deleteAll();
        savingsRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        adminRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }



    @Test
    void updateBalance_correctDecrease() throws Exception {

        BalanceDTO balanceDTO = new BalanceDTO(new BigDecimal(3000));
        String body = objectMapper.writeValueAsString(balanceDTO);
        MvcResult mvcResult = mockMvc.perform(
                patch("/updatebalance/"+account1.getAccountId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("500"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ADMINFROMACCOUNT"));
    }

    @Test
    void updateBalance_correctIncrease() throws Exception {

        BalanceDTO balanceDTO = new BalanceDTO(new BigDecimal("4521.36"));
        String body = objectMapper.writeValueAsString(balanceDTO);
        MvcResult mvcResult = mockMvc.perform(
                patch("/updatebalance/"+account1.getAccountId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("1021.36"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ADMINTOACCOUNT"));
    }

    @Test
    void transferFunds_sufficientFunds_correctValidation() throws Exception {

        TransferDTO transferDTO = new TransferDTO(account2.getAccountId(), user2.getName(), new BigDecimal(500));
        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/transferfunds/"+account1.getAccountId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ACCOUNTTOACCOUNT"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("500"));
    }

    @Test
    void transferFunds_insufficientFunds_correctValidation() throws Exception {

        TransferDTO transferDTO = new TransferDTO(account2.getAccountId(), user2.getName(), new BigDecimal(50000));
        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/transferfunds/"+account1.getAccountId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void transferFunds_sufficientFunds_incorrectValidation() throws Exception {

        TransferDTO transferDTO = new TransferDTO(account2.getAccountId(), user1.getName(), new BigDecimal(500));
        String body = objectMapper.writeValueAsString(transferDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/transferfunds/"+account1.getAccountId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void sendFunds_correctValidation() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKey");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/sendfunds")
                        .header("hashkey","T3stTh4rd")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("THIRDPARTYTOACCOUNT"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("300"));

    }

    @Test
    void sendFunds_incorrectValidation_secretKey() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKeyWrong");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/sendfunds")
                        .header("hashkey","T3stTh4rd")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void sendFunds_incorrectValidation_hashKey() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKey");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/sendfunds")
                        .header("hashkey","T3stTh4rdWrong")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void sendFunds_missingheader() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKey");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/sendfunds")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void claimFunds_correctValidation() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKey");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/claimfunds")
                        .header("hashkey","T3stTh4rd")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ACCOUNTTOTHIRDPARTY"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("-300"));

    }

    @Test
    void claimFunds_incorrectValidation_secretKey() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKeyWrong");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/claimfunds")
                        .header("hashkey","T3stTh4rd")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void claimFunds_incorrectValidation_hashKey() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKey");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/claimfunds")
                        .header("hashkey","T3stTh4rdWrong")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }

    @Test
    void claimFunds_missingheader() throws Exception {

        ThirdPartyTransactionDTO thirdPartyDTO = new ThirdPartyTransactionDTO(new BigDecimal(300),account2.getAccountId(), "SomeSecretKey");
        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/claimfunds")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest()).andReturn();

    }




}
