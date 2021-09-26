package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.interfaces.ITransactionController;
import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.dao.accounts.Transactions;
import com.ironhack.midtermproject.enums.TransactionTypes;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.TransactionsRepository;
import com.ironhack.midtermproject.repository.UserRepository;
import com.ironhack.midtermproject.utils.AccountUtility;
import com.ironhack.midtermproject.utils.Money;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class TransactionController implements ITransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private AccountUtility accountUtility;

    @Autowired
    private UserRepository userRepository;

    @PatchMapping("/updatebalance/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBalance(@PathVariable(name= "accountId") Long accountId, @RequestBody @Valid BalanceDTO balanceDTO){

        Optional<Account> account = accountRepository.findById(accountId);

        if(account.isPresent()){
            Money balanceDifference = new Money(balanceDTO.getBalance().subtract(account.get().getBalance().getAmount()));
            TransactionTypes transactionTypes;
            if(balanceDifference.getAmount().compareTo(new BigDecimal(0))>0){
                transactionTypes = TransactionTypes.ADMINTOACCOUNT;
            }else{
                transactionTypes = TransactionTypes.ADMINFROMACCOUNT;
            }
            account.get().setBalance(new Money(balanceDTO.getBalance()));
            Transactions adminTransaction = new Transactions(balanceDifference,account.get(),transactionTypes, userRepository.getById(1L));
            transactionsRepository.save(adminTransaction);
            accountRepository.save(account.get());
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Account for that reference");
        }
    }

}
