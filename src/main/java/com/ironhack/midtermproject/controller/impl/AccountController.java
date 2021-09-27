package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.dto.AccountDTO;
import com.ironhack.midtermproject.controller.interfaces.IAccountController;
import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.dao.roles.Users;
import com.ironhack.midtermproject.repository.UserRepository;
import com.ironhack.midtermproject.service.interfaces.IAccountService;
import com.ironhack.midtermproject.utils.AccountUtility;
import com.ironhack.midtermproject.utils.Money;
import com.ironhack.midtermproject.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController implements IAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountUtility accountUtility;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private UserRepository userRepository;

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

    // Get path for account holders to access their accounts - using currently logged in user
    @GetMapping("/myaccounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Object[]> getAccountsUser(Principal principal) {

        Optional<Users> authorisedUser = userRepository.findByUsername(principal.getName());

        if (authorisedUser.isPresent()) {

        Long userId = authorisedUser.get().getUserId();

        List<Object[]> list = accountRepository.findAccountsForCustomerId(userId);

        //apply Interest, Maintenance Fees, and account Penalties before displaying balance to user.
        for (Object[] result : list) {
            accountUtility.applyInterest(((BigInteger) result[0]).longValue());
            accountUtility.applyMaintenance(((BigInteger) result[0]).longValue());
            accountUtility.applyPenalty(((BigInteger) result[0]).longValue(), new Money((BigDecimal) result[2]));
        }

        return accountRepository.findAccountsForCustomerId(userId);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something has gone wrong");
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

    //Post Path for admins to create new accounts, takes a path variable and body
    @PostMapping("/newaccount/{accountType}")
    @ResponseStatus(HttpStatus.CREATED)
    public Account newAccount(@PathVariable(name = "accountType") String accountType, @RequestBody @Valid AccountDTO accountDTO){

       if(accountType.toUpperCase().equals("CHECKING")){
        return accountService.checkingAccount(new Money(accountDTO.getBalance()),accountDTO.getSecretKey(),accountDTO.getPrimaryId(),accountDTO.getSecondaryId());

       }else if(accountType.toUpperCase().equals("SAVINGS")){

        return accountService.savingsAccount(new Money(accountDTO.getBalance()),accountDTO.getSecretKey(),accountDTO.getPrimaryId(), accountDTO.getSecondaryId(),accountDTO.getInterestRate(), accountDTO.getMinimumBalance());

           } else if(accountType.toUpperCase().equals("CREDITCARD")){

            return accountService.creditCard(new Money(accountDTO.getBalance()),accountDTO.getPrimaryId(),accountDTO.getSecondaryId(), accountDTO.getInterestRate(), accountDTO.getCreditLimit());

                }

       throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something has gone wrong");
    }
}
