package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.repository.AccountHolderRepository;
import com.ironhack.midtermproject.repository.AddressRepository;
import com.ironhack.midtermproject.repository.CheckingAccountRepository;
import com.ironhack.midtermproject.repository.CreditCardRepository;
import org.junit.Assert;
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
class CreditCardTest {


    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    CreditCard account1;

    @BeforeEach
    void setUp() {

        Address address1 = new Address("Test Street","Test Postcode");
        addressRepository.save(address1);
        AccountHolder user1 = new AccountHolder("Test Customer1", LocalDate.of(2014,2,11),address1);
        accountHolderRepository.save(user1);
        account1 = new CreditCard(new Money(new BigDecimal(3500)),user1);
        creditCardRepository.save(account1);

    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();

    }

    @Test
    void setInterestRate_valid() {
        account1.setInterestRate(new BigDecimal(0.15));
        assertEquals(new BigDecimal(0.15), account1.getInterestRate());
    }

    @Test
    void setInterestRate_invalid() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {account1.setInterestRate(new BigDecimal(0.3));});
    }

    @Test
    void setCreditLimit_valid() {
        account1.setCreditLimit(new BigDecimal(500.00));
        assertEquals(new BigDecimal(500.00).floatValue(), account1.getCreditLimit().getAmount().floatValue());
    }

    @Test
    void setCreditLimit_invalid() {
        Assert.assertThrows(IllegalArgumentException.class, () -> {account1.setCreditLimit((new BigDecimal(150000)));});
    }

}