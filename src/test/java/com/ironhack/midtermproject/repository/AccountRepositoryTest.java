package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.dao.roles.Address;
import com.ironhack.midtermproject.dao.accounts.CheckingAccount;
import com.ironhack.midtermproject.utils.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;

    @Autowired
    private AccountRepository accountRepository;

    private AccountHolder user1;
    private AccountHolder user2;

    private CheckingAccount account1;
    private CheckingAccount account2;

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
    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void findAccountsForCustomerId() {
        var accountsByCustomer = accountRepository.findAccountsForCustomerId(user1.getUserId());
        assertEquals(BigInteger.valueOf(account1.getAccountId()),accountsByCustomer.get(0)[0]);
        assertEquals("USD", accountsByCustomer.get(0)[1]);
        assertEquals(new BigDecimal(3500.00).setScale(2), accountsByCustomer.get(0)[2]);
    }
}