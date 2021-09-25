package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.dao.accounts.*;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.repository.*;
import com.ironhack.midtermproject.service.interfaces.IAccountService;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService implements IAccountService {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private AccountHolderRepository accountHolderRepository;


    //Checking account
    public Account checkingAccount(Money balance, String secretKey, Long primaryId, Long secondaryId){

        Optional<AccountHolder> primaryAccountHolder = accountHolderRepository.findById(primaryId);
        Optional<AccountHolder> secondaryAccountHolder = Optional.empty();
        if(secondaryId!= null) {
            secondaryAccountHolder = accountHolderRepository.findById(secondaryId);
        }

        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();
        int currentDay = LocalDateTime.now().getDayOfMonth();
        int primaryAge;
        int secondaryAge;

        if(primaryAccountHolder.isPresent()){
            LocalDate primaryDOB = primaryAccountHolder.get().getDateOfBirth();
            primaryAge = currentYear - primaryDOB.getYear();
            if(currentMonth<primaryDOB.getMonthValue()){
                primaryAge--;
            }else if(currentMonth==primaryDOB.getMonthValue() && currentDay<primaryDOB.getDayOfMonth()){
                primaryAge--;
            }

            if(secondaryAccountHolder.isPresent()){
                LocalDate secondaryDOB = secondaryAccountHolder.get().getDateOfBirth();
                secondaryAge = currentYear - secondaryDOB.getYear();
                if(currentMonth<secondaryDOB.getMonthValue()){
                    secondaryAge--;
                }else if(currentMonth==secondaryDOB.getMonthValue() && currentDay<secondaryDOB.getDayOfMonth()){
                    secondaryAge--;
                }

                if(primaryAge>=24 && secondaryAge>=24){
                CheckingAccount newCheckingTwoHolder = new CheckingAccount(balance, primaryAccountHolder.get(),secondaryAccountHolder.get(), secretKey);
                return checkingAccountRepository.save(newCheckingTwoHolder);
                }
                else{
                    StudentChecking newStudentCheckingTwoHolder = new StudentChecking(balance, primaryAccountHolder.get(),secondaryAccountHolder.get(), secretKey);
                    return studentCheckingRepository.save(newStudentCheckingTwoHolder);
                }

            }else {
                if (primaryAge >= 24) {
                CheckingAccount newCheckingOneHolder = new CheckingAccount(balance, primaryAccountHolder.get(), secretKey);
                return checkingAccountRepository.save(newCheckingOneHolder);
                } else{
                    StudentChecking newStudentCheckingOneHolder = new StudentChecking(balance, primaryAccountHolder.get(), secretKey);
                    return studentCheckingRepository.save(newStudentCheckingOneHolder);
                }
            }
        }

        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Something has gone wrong");
        }
    }

    //Savings Account

    public Account savingsAccount(Money balance, String secretKey, Long primaryId, Long secondaryId, BigDecimal interestRate, BigDecimal minimumBalance) {

        Optional<AccountHolder> primaryAccountHolder = accountHolderRepository.findById(primaryId);
        Optional<AccountHolder> secondaryAccountHolder = Optional.empty();
        if (secondaryId != null) {
            secondaryAccountHolder = accountHolderRepository.findById(secondaryId);
        }

        //check for bad input for interest rate or minimum balance

        if(minimumBalance != null && (minimumBalance.compareTo(new BigDecimal(100))<0 || minimumBalance.compareTo(new BigDecimal(1000))>0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MinimumBalance outside accepted range");
        }

        if(interestRate != null && (interestRate.compareTo(new BigDecimal(0))<0 || interestRate.compareTo(new BigDecimal(0.5))>0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "interestRate outside accepted range");
        }


        if (primaryAccountHolder.isPresent()) {
            //Check for minimum balance
            if (minimumBalance != null) {

                //Check for interest rate
                if (interestRate != null) {
                    if (secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), secondaryAccountHolder.get(), interestRate, secretKey, minimumBalance);
                        return savingsRepository.save(savingAccount);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), interestRate, secretKey, minimumBalance);
                        return savingsRepository.save(savingAccount);
                    }
                } else {
                    if (secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), secondaryAccountHolder.get(), secretKey, minimumBalance);
                        return savingsRepository.save(savingAccount);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), secretKey, minimumBalance);
                        return savingsRepository.save(savingAccount);
                    }
                }
            } else {
                if (interestRate != null) {
                    if (secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), secondaryAccountHolder.get(), interestRate, secretKey);
                        return savingsRepository.save(savingAccount);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), interestRate, secretKey);
                        return savingsRepository.save(savingAccount);
                    }
                } else {
                    if (secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), secondaryAccountHolder.get(), secretKey);
                        return savingsRepository.save(savingAccount);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        Savings savingAccount = new Savings(balance, primaryAccountHolder.get(), secretKey);
                        return savingsRepository.save(savingAccount);
                    }
                }
            }

        }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something has gone wrong");

    }

    //Credit Card

    public Account creditCard(Money balance, Long primaryId, Long secondaryId, BigDecimal interestRate, BigDecimal creditLimit) {

        Optional<AccountHolder> primaryAccountHolder = accountHolderRepository.findById(primaryId);
        Optional<AccountHolder> secondaryAccountHolder = Optional.empty();
        if (secondaryId != null) {
            secondaryAccountHolder = accountHolderRepository.findById(secondaryId);
        }

//        error checking
        if(creditLimit != null && (creditLimit.compareTo(new BigDecimal(100))<0 || creditLimit.compareTo(new BigDecimal(100000))>0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CreditLimit outside accepted range");
        }

        if(interestRate != null && (interestRate.compareTo(new BigDecimal(0.1))<0 || interestRate.compareTo(new BigDecimal(0.2))>0)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "interestRate outside accepted range");
        }

        if(creditLimit!= null) {
            if (balance.getAmount().compareTo(creditLimit) > 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "balance exceeds Credit Limit");
            }
        }else if(balance.getAmount().compareTo(new BigDecimal(100))>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "balance exceeds default Credit Limit");
        }

        if (primaryAccountHolder.isPresent()) {
            //Check for minimum balance
            if (creditLimit != null) {

                //Check for interest rate
                if (interestRate != null) {
                    if (secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get(), secondaryAccountHolder.get(), interestRate, creditLimit);
                        return creditCardRepository.save(creditCard);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get(), interestRate, creditLimit);
                        return creditCardRepository.save(creditCard);
                    }
                } else {
                    if (secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get(), secondaryAccountHolder.get(), creditLimit);
                        return creditCardRepository.save(creditCard);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get(), creditLimit);
                        return creditCardRepository.save(creditCard);
                    }
                }
            } else {
                if (interestRate != null) {
                    if (secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get(), secondaryAccountHolder.get(), interestRate);
                        return creditCardRepository.save(creditCard);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get(), interestRate);
                        return creditCardRepository.save(creditCard);
                    }
                } else {
                    if (secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get(), secondaryAccountHolder.get());
                        return creditCardRepository.save(creditCard);
                    } else if (!secondaryAccountHolder.isPresent()) {
                        CreditCard creditCard = new CreditCard(balance, primaryAccountHolder.get());
                        return creditCardRepository.save(creditCard);
                    }
                }
            }

        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something has gone wrong");
    }






}
