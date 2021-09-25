package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.utils.Money;

import java.math.BigDecimal;

public interface IAccountService {

    Account checkingAccount(Money balance, String secretKey, Long primaryId, Long secondaryId);

    Account savingsAccount(Money balance, String secretKey, Long primaryId, Long secondaryId, BigDecimal interestRate, BigDecimal minimumBalance);

    Account creditCard(Money balance, Long primaryId, Long secondaryId, BigDecimal interestRate, BigDecimal creditLimit);
}
