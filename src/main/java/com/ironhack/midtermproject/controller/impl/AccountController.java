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

    @GetMapping("/accounts/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Object[]> getAccounts(@PathVariable(name = "userId") Long userId){
        return accountRepository.findAccountsForCustomerId(userId);
    }
}
