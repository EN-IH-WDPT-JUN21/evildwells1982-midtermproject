package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.repository.AccountHolderRepository;
import com.ironhack.midtermproject.repository.AddressRepository;
import com.ironhack.midtermproject.repository.CheckingAccountRepository;
import com.ironhack.midtermproject.repository.SavingsRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SavingsTest {

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    Savings account1;


    @BeforeEach
    void setUp() {

        Address address1 = new Address("Test Street","Test Postcode");
        addressRepository.save(address1);
        AccountHolder user1 = new AccountHolder("Test Customer1", LocalDate.of(2014,2,11),address1);
        accountHolderRepository.save(user1);
        account1 = new Savings(new Money(new BigDecimal(3500)),user1,"Test Secret Key");
        savingsRepository.save(account1);

    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        savingsRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void setInterestRate_valid() {
        account1.setInterestRate(new BigDecimal(0.5));
        assertEquals(new BigDecimal(0.5), account1.getInterestRate());
    }

    @Test
    void setInterestRate_invalid() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {account1.setInterestRate(new BigDecimal(0.9));});
    }

    @Test
    void setMinimumBalance_valid() {
        account1.setMinimumBalance(new BigDecimal(500.00));
        assertEquals(new BigDecimal(500.00).floatValue(), account1.getMinimumBalance().getAmount().floatValue());
    }

    @Test
    void setMinimumBalance_invalid() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {account1.setMinimumBalance(new BigDecimal(99));});
    }
}