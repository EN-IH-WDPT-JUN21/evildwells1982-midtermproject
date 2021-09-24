package com.ironhack.midtermproject.service.interfaces;

import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.utils.Money;

public interface IAccountService {

    Account checkingAccount(Money balance, String secretKey, Long primaryId, Long secondaryId);
}
