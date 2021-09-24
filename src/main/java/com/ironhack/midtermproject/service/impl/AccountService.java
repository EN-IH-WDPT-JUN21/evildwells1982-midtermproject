package com.ironhack.midtermproject.service.impl;

import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.dao.accounts.CheckingAccount;
import com.ironhack.midtermproject.dao.accounts.StudentChecking;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.repository.*;
import com.ironhack.midtermproject.service.interfaces.IAccountService;
import com.ironhack.midtermproject.utils.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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




}
