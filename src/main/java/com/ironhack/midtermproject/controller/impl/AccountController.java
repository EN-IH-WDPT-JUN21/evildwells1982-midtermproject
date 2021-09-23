package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.IAccountController;
import com.ironhack.midtermproject.utils.AccountUtility;
import com.ironhack.midtermproject.utils.Money;
import com.ironhack.midtermproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
public class AccountController implements IAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountUtility accountUtility;

    // Get path for account holders to access their accounts
    @GetMapping("/accounts/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Object[]> getAccounts(@PathVariable(name = "userId") Long userId){

        List<Object[]> list = accountRepository.findAccountsForCustomerId(userId);

        //apply Interest, Maintenance Fees, and account Penalties before displaying balance to user.
        for (Object[] result : list) {
            accountUtility.applyInterest(((BigInteger)result[0]).longValue());
            accountUtility.applyMaintenance(((BigInteger)result[0]).longValue());
            accountUtility.applyPenalty(((BigInteger)result[0]).longValue(),new Money((BigDecimal)result[2]));
        }

        return accountRepository.findAccountsForCustomerId(userId);
    }

    // Get path for admins to access account balance with param account id
    @GetMapping("/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Object[]> getAccountsById(@PathVariable(name = "accountId") Long accountId){

        List<Object[]> list = accountRepository.findAccountsForAccountId(accountId);

        //apply Interest, Maintenance Fees, and account Penalties before displaying balance to user.
        for (Object[] result : list) {
            accountUtility.applyInterest(((BigInteger)result[0]).longValue());
            accountUtility.applyMaintenance(((BigInteger)result[0]).longValue());
            accountUtility.applyPenalty(((BigInteger)result[0]).longValue(),new Money((BigDecimal)result[2]));
        }

        return accountRepository.findAccountsForAccountId(accountId);
    }
}
