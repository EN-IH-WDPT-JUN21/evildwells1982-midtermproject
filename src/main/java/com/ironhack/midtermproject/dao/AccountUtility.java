package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.enums.TransactionTypes;
import com.ironhack.midtermproject.interfaces.Interest;
import com.ironhack.midtermproject.interfaces.Maintenance;
import com.ironhack.midtermproject.interfaces.Penalties;
import com.ironhack.midtermproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountUtility implements Interest, Maintenance, Penalties {

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;


    public void applyInterest(Long accountId){

        Optional<Savings> savingsOptional = savingsRepository.findById(accountId);
        Optional<CreditCard> creditCardOptional = creditCardRepository.findById(accountId);

        Optional<LocalDateTime> lastInterest = transactionsRepository.findLastInterest(accountId);


        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();
        int currentDay = LocalDateTime.now().getDayOfMonth();
        int elapsedYears;


        //For Savings Account, find number of complete elapsed years between now and the creation date, or last interest date,
        // apply compound interest for each elapsed year, then create a new interest transaction for that amount and update the balance

        if(savingsOptional.isPresent()){
            if(savingsOptional.get().getCreationDate().getYear()!=currentYear){
                Money startingBalance = savingsOptional.get().getBalance();
                LocalDate creationDate = savingsOptional.get().getCreationDate();
                if(lastInterest.isPresent()){
                    elapsedYears = currentYear - lastInterest.get().getYear();
                    if(currentMonth<lastInterest.get().getMonthValue()){
                        elapsedYears--;
                    }else if(currentMonth==lastInterest.get().getMonthValue() && currentDay<lastInterest.get().getDayOfMonth()){
                        elapsedYears--;
                    }
                }else{
                    elapsedYears = currentYear - creationDate.getYear();
                    if(currentMonth<creationDate.getMonthValue()){
                        elapsedYears--;
                    }else if(currentMonth==creationDate.getMonthValue() && currentDay<creationDate.getDayOfMonth()){
                        elapsedYears--;
                    }
                }

                for(int i=0; i<elapsedYears;i++){
                    savingsOptional.get().setBalance(
                            new Money(savingsOptional.get().getBalance().increaseAmount(
                                    savingsOptional.get().getBalance().getAmount()
                                            .multiply(savingsOptional.get().getInterestRate()))));
                }
                savingsRepository.save(savingsOptional.get());
                Money appliedInterest = new Money(savingsOptional.get().getBalance().getAmount().subtract(startingBalance.getAmount()));
                Transactions interestTransaction = new Transactions(savingsOptional.get(),appliedInterest);
                transactionsRepository.save(interestTransaction);
            }
        }

    }

    public void applyMaintenance(){


    }

    //Applies Penalty Fees to eligible accounts, and adds a transaction into the transactions table
    public void applyPenalty(Long accountId, Money startBalance, Money endBalance){

        Optional<CheckingAccount> checkingAccountOptional = checkingAccountRepository.findById(accountId);
        Optional<Savings> savingsOptional = savingsRepository.findById(accountId);

        if(checkingAccountOptional.isPresent()){
            Money checkingMinimum = checkingAccountOptional.get().getMinimumBalance();
            Money checkingPenalty = checkingAccountOptional.get().getPenaltyFee();
            if(startBalance.getAmount().compareTo(checkingMinimum.getAmount())>0 && endBalance.getAmount().compareTo(checkingMinimum.getAmount())<0){
                checkingAccountOptional.get().setBalance(new Money(checkingAccountOptional.get().getBalance().decreaseAmount(checkingPenalty)));
                checkingAccountRepository.save(checkingAccountOptional.get());
                Transactions penaltyTransaction = new Transactions(checkingAccountOptional.get(),checkingPenalty, TransactionTypes.PENALTY);
                transactionsRepository.save(penaltyTransaction);
            }
        } else if(savingsOptional.isPresent()){
            Money savingsMinimum = savingsOptional.get().getMinimumBalance();
            Money savingsPenalty = savingsOptional.get().getPenaltyFee();
            if(startBalance.getAmount().compareTo(savingsMinimum.getAmount())>0 && endBalance.getAmount().compareTo(savingsMinimum.getAmount())<0){
                savingsOptional.get().setBalance(new Money(savingsOptional.get().getBalance().decreaseAmount(savingsPenalty)));
                savingsRepository.save(savingsOptional.get());
                Transactions penaltyTransaction = new Transactions(savingsOptional.get(),savingsPenalty, TransactionTypes.PENALTY);
                transactionsRepository.save(penaltyTransaction);
            }
        }

    }


}
