package com.ironhack.midtermproject.utils;

import com.ironhack.midtermproject.dao.accounts.*;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.dao.roles.Address;
import com.ironhack.midtermproject.enums.AccountStatus;
import com.ironhack.midtermproject.enums.TransactionTypes;
import com.ironhack.midtermproject.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FraudDetectionTest {


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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FraudDetection fraudDetection;


    private AccountHolder user1;
    private AccountHolder user2;

    private CheckingAccount account1;
    private CheckingAccount account2;
    private Savings account3;
    private CreditCard account4;
    private StudentChecking account5;

    private Transactions maintenance1;

    private Transactions interest1;

    private Transactions transfer1;
    private Transactions transfer2;
    private Transactions transfer3;
    private Transactions transfer4;



    @BeforeEach
    void setUp() {
        Address address1 = new Address("Test Street", "Test Postcode");
        addressRepository.save(address1);
        user1 = new AccountHolder("Test Customer1", LocalDate.of(2014, 2, 11), address1);
        accountHolderRepository.save(user1);
        account1 = new CheckingAccount(new Money(new BigDecimal(3500)), user1, "SomeSecretKey");
        checkingAccountRepository.save(account1);

        Address address2 = new Address("Test Road", "Test Post2");
        addressRepository.save(address2);
        user2 = new AccountHolder("Test Customer2", LocalDate.of(2014, 2, 11), address2);
        accountHolderRepository.save(user2);
        account2 = new CheckingAccount(new Money(new BigDecimal(2500)), user2, "SomeSecretKey");
        checkingAccountRepository.save(account2);

        account3 = new Savings(new Money(new BigDecimal(5000)), user1, "SomeSecretKey");
        savingsRepository.save(account3);

        account4 = new CreditCard(new Money(new BigDecimal(1000)), user1);
        creditCardRepository.save(account4);

        account5 = new StudentChecking(new Money(new BigDecimal(2500)), user2, "SomeSecretKey");
        studentCheckingRepository.save(account5);

        maintenance1 = new Transactions(account1,account1.getMonthlyMaintenanceFee(), TransactionTypes.MAINTENANCE);
        transactionsRepository.save(maintenance1);

        interest1 = new Transactions(account1,new Money(new BigDecimal(500)));
        transactionsRepository.save(interest1);

        transfer1 = new Transactions(account1,account2,new Money(new BigDecimal(500)), user1);
        transfer2 = new Transactions(account1,account2,new Money(new BigDecimal(500)), user1);
        transfer3 = new Transactions(account1,account2,new Money(new BigDecimal(500)), user1);
        transfer4 = new Transactions(account1,account2,new Money(new BigDecimal(500)), user1);

        transactionsRepository.save(transfer1);
        transactionsRepository.save(transfer2);
        transactionsRepository.save(transfer3);
        transactionsRepository.save(transfer4);

    }

    @AfterEach
    void tearDown() {
        transactionsRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        savingsRepository.deleteAll();
        studentCheckingRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountHolderRepository.deleteAll();
        userRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void checkTransactionsInOneSecond_Destination() {
    Transactions testTransaction = new Transactions(account1,account2,new Money(new BigDecimal(500)),user1);
    Exception exception = assertThrows(ResponseStatusException.class, () -> {fraudDetection.checkTransactionsInOneSecond(account1.getAccountId(),null, testTransaction.getTransactionDateTime());});
    String expectedMessage = "Fraud Detection, Destination Account Frozen";
    String actualMessage = exception.getMessage();
    assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkTransactionsInOneSecond_Origin() {
        Transactions testTransaction = new Transactions(account2,account1,new Money(new BigDecimal(500)),user1);
        Exception exception = assertThrows(ResponseStatusException.class, () -> {fraudDetection.checkTransactionsInOneSecond(null,account1.getAccountId(), testTransaction.getTransactionDateTime());});
        String expectedMessage = "Fraud Detection, Origin Account Frozen";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void checkMaxTransactionsIn24Hours_Destination() {
        Transactions testTransaction = new Transactions(account2,account1,new Money(new BigDecimal(3000)),user1);
        //fraudDetection.checkTransactionsInOneSecond(account1.getAccountId(),null, testTransaction.getTransactionDateTime());
        //assertEquals(AccountStatus.FROZEN,accountRepository.getById(account1.getAccountId()).getAccountStatus());
        Exception exception = assertThrows(ResponseStatusException.class, () -> {fraudDetection.checkMaxTransactionsIn24Hours(account1.getAccountId(), null, testTransaction.getTransactionDateTime(), testTransaction.getTransactionAmount().getAmount());});
        String expectedMessage = "Fraud Detection, Destination Account Frozen";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkMaxTransactionsIn24Hours_Origin() {
        Transactions testTransaction = new Transactions(account2,account1,new Money(new BigDecimal(3000)),user1);
        //fraudDetection.checkTransactionsInOneSecond(account1.getAccountId(),null, testTransaction.getTransactionDateTime());
        //assertEquals(AccountStatus.FROZEN,accountRepository.getById(account1.getAccountId()).getAccountStatus());
        Exception exception = assertThrows(ResponseStatusException.class, () -> {fraudDetection.checkMaxTransactionsIn24Hours( null, account1.getAccountId(), testTransaction.getTransactionDateTime(), testTransaction.getTransactionAmount().getAmount());});
        String expectedMessage = "Fraud Detection, Origin Account Frozen";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}