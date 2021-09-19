package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.IAccountController;
import com.ironhack.midtermproject.dao.Account;
import com.ironhack.midtermproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountController implements IAccountController {

    @Autowired
    private AccountRepository accountRepository;

    // Get path for account holders to access their accounts
    @GetMapping("/accounts/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Object[]> getAccounts(@PathVariable(name = "userId") Long userId){
        return accountRepository.findAccountsForCustomerId(userId);
    }

    // Get path for admins to access account balance with param account id
    @GetMapping("/account/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Object[]> getAccountsById(@PathVariable(name = "accountId") Long accountId){
        return accountRepository.findAccountsForAccountId(accountId);
    }
}
