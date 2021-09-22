package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.enums.TransactionTypes;
import com.ironhack.midtermproject.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Savings account6;
    private CreditCard account4;
    private CreditCard account7;
    private StudentChecking account5;
    private Transactions interest1;
    private Transactions interest2;
    private Transactions maintenance1;

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

        interest1 = new Transactions(account3,new Money(new BigDecimal(500)));
        transactionsRepository.save(interest1);

        account4 = new CreditCard(new Money(new BigDecimal(1000)),user1);
        creditCardRepository.save(account4);

        interest2 = new Transactions(account4,new Money(new BigDecimal(50)));
        transactionsRepository.save(interest2);

        account5 = new StudentChecking(new Money(new BigDecimal(2500)),user2,"SomeSecretKey");
        studentCheckingRepository.save(account5);

        account6 = new Savings(new Money(new BigDecimal(5000)),user1,"SomeSecretKey");
        savingsRepository.save(account6);

        account7 = new CreditCard(new Money(new BigDecimal(1000)),user1);
        creditCardRepository.save(account7);

        maintenance1 = new Transactions(account1,new Money(new BigDecimal(12)),TransactionTypes.MAINTENANCE);
        transactionsRepository.save(maintenance1);

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
    void applyInterest_Savings_createdSameYear_noInterestApplied() {
    accountUtility.applyInterest(account6.getAccountId());
    BigDecimal resultBalance = (savingsRepository.getById(account6.getAccountId()).getBalance().getAmount());
    assertEquals(new BigDecimal(5000).setScale(2), resultBalance);
    }

    @Test
    void applyInterest_Savings_lastInterestInSameYear_noInterestApplied() {
        accountUtility.applyInterest(account3.getAccountId());
        BigDecimal resultBalance = (savingsRepository.getById(account3.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(5000).setScale(2), resultBalance);
    }

    @Test
    void applyInterest_Savings_noPreviousInterestAccount2YearsOld() {

        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        accountRepository.updateCreationDate(twoYearsAgo,account6.getAccountId());
        accountUtility.applyInterest(account6.getAccountId());
        BigDecimal resultBalance = (savingsRepository.getById(account6.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(5025.03).setScale(2, RoundingMode.HALF_UP),resultBalance);
    }

    @Test
    void applyInterest_Savings_lastInterestOneYearAgo() {

        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        LocalDateTime oneYearAgo = LocalDateTime.of(now.getYear()-1,now.getMonthValue(),now.getDayOfMonth(),now.getHour(), now.getMinute());
        accountRepository.updateCreationDate(twoYearsAgo,account3.getAccountId());
        transactionsRepository.updateTransactionDate(oneYearAgo,interest1.getTransactionId());
        accountUtility.applyInterest(account3.getAccountId());
        BigDecimal resultBalance = (savingsRepository.getById(account3.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(5012.50).setScale(2), resultBalance);
    }

    @Test
    void applyInterest_CreditCard_createdSameYear_noInterestApplied() {
        accountUtility.applyInterest(account7.getAccountId());
        BigDecimal resultBalance = (creditCardRepository.getById(account7.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(1000).setScale(2), resultBalance);
    }

    @Test
    void applyInterest_CreditCard_lastInterestInSameYear_noInterestApplied() {
        accountUtility.applyInterest(account4.getAccountId());
        BigDecimal resultBalance = (creditCardRepository.getById(account4.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(1000).setScale(2), resultBalance);
    }

    @Test
    void applyInterest_CreditCard_noPreviousInterestAccount2YearsOld() {

        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        accountRepository.updateCreationDate(twoYearsAgo,account7.getAccountId());
        accountUtility.applyInterest(account7.getAccountId());
        BigDecimal resultBalance = (creditCardRepository.getById(account7.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(1486.91).setScale(2, RoundingMode.HALF_UP),resultBalance);
    }

    @Test
    void applyInterest_CreditCard_lastInterestOneYearAgo() {

        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        LocalDateTime oneYearAgo = LocalDateTime.of(now.getYear()-1,now.getMonthValue(),now.getDayOfMonth(),now.getHour(), now.getMinute());
        accountRepository.updateCreationDate(twoYearsAgo,account4.getAccountId());
        transactionsRepository.updateTransactionDate(oneYearAgo,interest2.getTransactionId());
        accountUtility.applyInterest(account4.getAccountId());
        BigDecimal resultBalance = (creditCardRepository.getById(account4.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(1219.38).setScale(2,RoundingMode.HALF_UP), resultBalance);
    }


    @Test
    void applyMaintenance_createdSameMonth_noMaintenanceApplied() {
        accountUtility.applyMaintenance(account2.getAccountId());
        BigDecimal resultBalance = (checkingAccountRepository.getById(account2.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(2500).setScale(2), resultBalance);
    }

    @Test
    void applyMaintenance_lastMaintenanceInSameMonth_noMaintenanceApplied() {
        accountUtility.applyMaintenance(account1.getAccountId());
        BigDecimal resultBalance = (checkingAccountRepository.getById(account1.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(3500).setScale(2), resultBalance);
    }

    @Test
    void applyMaintenance_noPreviousMaintenanceAccount2YearsOld() {

        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        accountRepository.updateCreationDate(twoYearsAgo,account2.getAccountId());
        accountUtility.applyMaintenance(account2.getAccountId());
        BigDecimal resultBalance = (checkingAccountRepository.getById(account2.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(2212).setScale(2, RoundingMode.HALF_UP),resultBalance);
    }

    @Test
    void applyMaintenance_lastMaintenanceOneYearAgo() {

        LocalDateTime now = LocalDateTime.now();
        LocalDate twoYearsAgo = LocalDate.of(now.getYear()-2,now.getMonthValue(),now.getDayOfMonth());
        LocalDateTime oneYearAgo = LocalDateTime.of(now.getYear()-1,now.getMonthValue(),now.getDayOfMonth(),now.getHour(), now.getMinute());
        accountRepository.updateCreationDate(twoYearsAgo,account1.getAccountId());
        transactionsRepository.updateTransactionDate(oneYearAgo,maintenance1.getTransactionId());
        accountUtility.applyMaintenance(account1.getAccountId());
        BigDecimal resultBalance = (checkingAccountRepository.getById(account1.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(3356).setScale(2,RoundingMode.HALF_UP), resultBalance);
    }

    @Test
    void applyPenalty_checkingAccount_belowMin() {
        account1.setBalance(new Money(new BigDecimal(100)));
        checkingAccountRepository.save(account1);
        accountUtility.applyPenalty(account1.getAccountId(),new Money(new BigDecimal(500)));
        BigDecimal resultBalance = (checkingAccountRepository.getById(account1.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(60).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_checkingAccount_aboveMin() {
        account1.setBalance(new Money(new BigDecimal(250)));
        checkingAccountRepository.save(account1);
        accountUtility.applyPenalty(account1.getAccountId(),new Money(new BigDecimal(500)));
        BigDecimal resultBalance = (checkingAccountRepository.getById(account1.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(250).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_savingsAccount() {
        account3.setBalance(new Money(new BigDecimal(100)));
        savingsRepository.save(account3);
        accountUtility.applyPenalty(account3.getAccountId(),new Money(new BigDecimal(1500)));
        BigDecimal resultBalance = (savingsRepository.getById(account3.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(60).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_studentCheckingAccount() {
        account5.setBalance(new Money(new BigDecimal(100)));
        studentCheckingRepository.save(account5);
        accountUtility.applyPenalty(account5.getAccountId(),new Money(new BigDecimal(500)));
        BigDecimal resultBalance = (studentCheckingRepository.getById(account5.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(100).setScale(2), resultBalance);
    }

    @Test
    void applyPenalty_creditCard() {
        account4.setBalance(new Money(new BigDecimal(100)));
        creditCardRepository.save(account4);
        accountUtility.applyPenalty(account4.getAccountId(),new Money(new BigDecimal(500)));
        BigDecimal resultBalance = (creditCardRepository.getById(account4.getAccountId()).getBalance().getAmount());
        assertEquals(new BigDecimal(100).setScale(2), resultBalance);
    }




}