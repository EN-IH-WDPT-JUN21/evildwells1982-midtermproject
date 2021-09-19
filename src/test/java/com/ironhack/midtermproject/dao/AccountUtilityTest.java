package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountUtilityTest {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    private AccountHolder user1;
    private AccountHolder user2;

    private CheckingAccount account1;
    private CheckingAccount account2;
    private Savings account3;
    private CreditCard account4;
    private StudentChecking account5;

    @Autowired
    private AccountUtility accountUtility;

    @BeforeEach
    void setUp() {
        Address address1 = new Address("Test Street","Test Postcode");
        addressRepository.save(address1);
        user1 = new AccountHolder("Test Customer1", LocalDate.of(2014,2,11),address1);
        accountHolderRepository.save(user1);
        account1 = new CheckingAccount(new Money(new BigDecimal(3500)),user1,"SomeSecretKey");
        checkingAccountRepository.save(account1);

        Address address2 = new Address("Test Road","Test Post2");
        addressRepository.save(address2);
        user2 = new AccountHolder("Test Customer2", LocalDate.of(2014,2,11),address2);
        accountHolderRepository.save(user2);
        account2 = new CheckingAccount(new Money(new BigDecimal(2500)),user2,"SomeSecretKey");
        checkingAccountRepository.save(account2);

        account3 = new Savings(new Money(new BigDecimal(5000)),user1,"SomeSecretKey");
        savingsRepository.save(account3);

        account4 = new CreditCard(new Money(new BigDecimal(1000)),user1);
        creditCardRepository.save(account4);

        account5 = new StudentChecking(new Money(new BigDecimal(2500)),user2,"SomeSecretKey");
        studentCheckingRepository.save(account5);

    }

    @AfterEach
    void tearDown() {
        transactionsRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        savingsRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void applyInterest() {
    }

    @Test
    void applyMaintenance() {
    }

    @Test
    void applyPenalty_checkingAccount_belowMin() {
        accountUtility.applyPenalty(account1.getAccountId(),new Money(new BigDecimal(500)), new Money(new BigDecimal(100)));
        BigDecimal resultBalance = (checkingAccountRepository.getById(account1.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(3460).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_checkingAccount_aboveMin() {
        accountUtility.applyPenalty(account1.getAccountId(),new Money(new BigDecimal(500)), new Money(new BigDecimal(250)));
        BigDecimal resultBalance = (checkingAccountRepository.getById(account1.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(3500).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_savingsAccount() {
        accountUtility.applyPenalty(account3.getAccountId(),new Money(new BigDecimal(1500)), new Money(new BigDecimal(100)));
        BigDecimal resultBalance = (savingsRepository.getById(account3.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(4960).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_studentCheckingAccount() {
        accountUtility.applyPenalty(account5.getAccountId(),new Money(new BigDecimal(500)), new Money(new BigDecimal(100)));
        BigDecimal resultBalance = (studentCheckingRepository.getById(account5.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(2500).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_creditCard() {
        accountUtility.applyPenalty(account4.getAccountId(),new Money(new BigDecimal(500)), new Money(new BigDecimal(100)));
        BigDecimal resultBalance = (creditCardRepository.getById(account4.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(1000).setScale(2), resultBalance);
    }


}