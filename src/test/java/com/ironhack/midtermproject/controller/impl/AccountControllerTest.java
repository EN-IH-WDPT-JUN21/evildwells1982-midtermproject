package com.ironhack.midtermproject.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.midtermproject.controller.dto.AccountDTO;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.dao.roles.Address;
import com.ironhack.midtermproject.dao.accounts.CheckingAccount;
import com.ironhack.midtermproject.utils.Money;
import com.ironhack.midtermproject.repository.*;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountControllerTest {

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

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private AccountHolder user1;
    private AccountHolder user2;
    private AccountHolder user3;

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

        Address address3 = new Address("Test Road","Test Post2");
        addressRepository.save(address3);
        user3 = new AccountHolder("Test Customer3", LocalDate.of(2020,2,11),address3);
        accountHolderRepository.save(user3);


    }

    @AfterEach
    void tearDown() {
        transactionsRepository.deleteAll();
        creditCardRepository.deleteAll();
        savingsRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void getAccounts_forCustomer() throws Exception{
        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        accountRepository.updateCreationDate(twoYearsAgo,account1.getAccountId());
        MvcResult mvcResult = mockMvc.perform(
                get("/accounts/"+user1.getUserId())
        ).andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("3212"));

    }

    @Test
    void getAccount_byId() throws Exception{
        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        accountRepository.updateCreationDate(twoYearsAgo,account2.getAccountId());
        MvcResult mvcResult = mockMvc.perform(
                get("/account/"+account2.getAccountId())
        ).andExpect(status().isOk()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("2212"));

    }

    @Test
    void post_studentChecking() throws Exception{
        AccountDTO accountDTO = new AccountDTO(new BigDecimal(3000),"S3cr3tK3Y",user3.getUserId());
        String body = objectMapper.writeValueAsString(accountDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/newaccount/"+"checking")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("S3cr3tK3Y"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("minimumBalance"));
    }

    @Test
    void post_checkingAccount() throws Exception{
        AccountDTO accountDTO = new AccountDTO(new BigDecimal(3000),"S3cr3tK3Y",user1.getUserId());
        String body = objectMapper.writeValueAsString(accountDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/newaccount/"+"checking")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("S3cr3tK3Y"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("minimumBalance"));
    }

    @Test
    void post_savingsAccount() throws Exception{
        AccountDTO accountDTO = new AccountDTO(new BigDecimal(3000),"S3cr3tK3Y",user1.getUserId());
        String body = objectMapper.writeValueAsString(accountDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/newaccount/"+"savings")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertTrue(mvcResult.getResponse().getContentAsString().contains("S3cr3tK3Y"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("interestRate"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.0025"));
    }

    @Test
    void post_creditCard() throws Exception{
        AccountDTO accountDTO = new AccountDTO(new BigDecimal(100),user1.getUserId());
        String body = objectMapper.writeValueAsString(accountDTO);
        MvcResult mvcResult = mockMvc.perform(
                post("/newaccount/"+"creditcard")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated()).andReturn();
        assertFalse(mvcResult.getResponse().getContentAsString().contains("secretKey"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("interestRate"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("0.2"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("creditLimit"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("100"));
    }





}