package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.enums.TransactionTypes;
import com.ironhack.midtermproject.interfaces.Interest;
import com.ironhack.midtermproject.interfaces.Maintenance;
import com.ironhack.midtermproject.interfaces.Penalties;
import com.ironhack.midtermproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
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
        int elapsedMonths;


        //For Savings Account, find number of complete elapsed years between now and the creation date, or last interest date,
        // apply compound interest for each elapsed year, then create a new interest transaction for that amount and update the balance

        if(savingsOptional.isPresent()){
            Money startingBalance = new Money(savingsOptional.get().getBalance().getAmount());
            if(savingsOptional.get().getCreationDate().getYear()!=currentYear){
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
                Money endBalance = new Money (savingsOptional.get().getBalance().getAmount());
                savingsRepository.save(savingsOptional.get());
                new Money(endBalance.decreaseAmount(startingBalance));
                if(endBalance.getAmount().floatValue()>0) {
                    Transactions interestTransaction = new Transactions(savingsOptional.get(), endBalance);
                    transactionsRepository.save(interestTransaction);
                }
            }
        }

        else if(creditCardOptional.isPresent()){
            Money startingBalance = new Money(creditCardOptional.get().getBalance().getAmount());
            if(creditCardOptional.get().getCreationDate().getMonthValue()!=currentMonth || creditCardOptional.get().getCreationDate().getYear()!=currentYear){
                LocalDate creationDate = creditCardOptional.get().getCreationDate();
                if(lastInterest.isPresent()){
                    elapsedYears = currentYear - lastInterest.get().getYear();
                    elapsedMonths = elapsedYears * 12;
                    elapsedMonths = elapsedMonths + (currentMonth-lastInterest.get().getMonthValue());
                    if(currentDay<lastInterest.get().getDayOfMonth()){
                        elapsedMonths --;
                    }
                }else{
                    elapsedYears = currentYear - creationDate.getYear();
                    elapsedMonths = elapsedYears * 12;
                    elapsedMonths = elapsedMonths + (currentMonth-creationDate.getMonthValue());
                    if(currentDay<creationDate.getDayOfMonth()){
                        elapsedMonths --;
                    }
                }

                for(int i=0; i<elapsedMonths;i++){
                    creditCardOptional.get().setBalance(
                            new Money(creditCardOptional.get().getBalance().increaseAmount(
                                    creditCardOptional.get().getBalance().getAmount()
                                            .multiply(creditCardOptional.get().getInterestRate().divide(new BigDecimal(12),6,RoundingMode.HALF_UP)))));
                }

                Money endBalance = new Money (creditCardOptional.get().getBalance().getAmount());
                creditCardRepository.save(creditCardOptional.get());
                new Money(endBalance.decreaseAmount(startingBalance));
                if(endBalance.getAmount().floatValue()>0) {
                    Transactions interestTransaction = new Transactions(creditCardOptional.get(), endBalance);
                    transactionsRepository.save(interestTransaction);
                }
            }
        }

    }

    public void applyMaintenance(Long accountId){

        Optional<CheckingAccount> checkingAccountOptional = checkingAccountRepository.findById(accountId);

        Optional<LocalDateTime> lastMaintenance = transactionsRepository.findLastMaintenance(accountId);

        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();
        int currentDay = LocalDateTime.now().getDayOfMonth();
        int elapsedYears;
        int elapsedMonths;


        if(checkingAccountOptional.isPresent()){
            Money startingBalance = new Money(checkingAccountOptional.get().getBalance().getAmount());
            if(checkingAccountOptional.get().getCreationDate().getMonthValue()!=currentMonth || checkingAccountOptional.get().getCreationDate().getYear()!=currentYear){
                LocalDate creationDate = checkingAccountOptional.get().getCreationDate();
                if(lastMaintenance.isPresent()){
                    elapsedYears = currentYear - lastMaintenance.get().getYear();
                    elapsedMonths = elapsedYears * 12;
                    elapsedMonths = elapsedMonths + (currentMonth-lastMaintenance.get().getMonthValue());
                    if(currentDay<lastMaintenance.get().getDayOfMonth()){
                        elapsedMonths --;
                    }
                }else{
                    elapsedYears = currentYear - creationDate.getYear();
                    elapsedMonths = elapsedYears * 12;
                    elapsedMonths = elapsedMonths + (currentMonth-creationDate.getMonthValue());
                    if(currentDay<creationDate.getDayOfMonth()){
                        elapsedMonths --;
                    }
                }

                for(int i=0; i<elapsedMonths;i++){
                    checkingAccountOptional.get().setBalance(
                            new Money(checkingAccountOptional.get().getBalance().decreaseAmount(
                                    checkingAccountOptional.get().getMonthlyMaintenanceFee()
                                            )));
                }

                Money endBalance = new Money (checkingAccountOptional.get().getBalance().getAmount());
                checkingAccountRepository.save(checkingAccountOptional.get());
                new Money(endBalance.decreaseAmount(startingBalance));
                if(endBalance.getAmount().floatValue()<0) {
                    Transactions maintenanceTransaction = new Transactions(checkingAccountOptional.get(), endBalance,TransactionTypes.MAINTENANCE);
                    transactionsRepository.save(maintenanceTransaction);
                }
            }
        }

    }

    //Applies Penalty Fees to eligible accounts, and adds a transaction into the transactions table
    public void applyPenalty(Long accountId, Money startBalance){

        Optional<CheckingAccount> checkingAccountOptional = checkingAccountRepository.findById(accountId);
        Optional<Savings> savingsOptional = savingsRepository.findById(accountId);

        if(checkingAccountOptional.isPresent()){
            Money checkingMinimum = checkingAccountOptional.get().getMinimumBalance();
            Money checkingPenalty = checkingAccountOptional.get().getPenaltyFee();
            Money endBalance = new Money(checkingAccountOptional.get().getBalance().getAmount());
            if(startBalance.getAmount().compareTo(checkingMinimum.getAmount())>0 && endBalance.getAmount().compareTo(checkingMinimum.getAmount())<0){
                checkingAccountOptional.get().setBalance(new Money(checkingAccountOptional.get().getBalance().decreaseAmount(checkingPenalty)));
                checkingAccountRepository.save(checkingAccountOptional.get());
                Transactions penaltyTransaction = new Transactions(checkingAccountOptional.get(),checkingPenalty, TransactionTypes.PENALTY);
                transactionsRepository.save(penaltyTransaction);
            }
        } else if(savingsOptional.isPresent()){
            Money savingsMinimum = savingsOptional.get().getMinimumBalance();
            Money savingsPenalty = savingsOptional.get().getPenaltyFee();
            Money endBalance = new Money(savingsOptional.get().getBalance().getAmount());
            if(startBalance.getAmount().compareTo(savingsMinimum.getAmount())>0 && endBalance.getAmount().compareTo(savingsMinimum.getAmount())<0){
                savingsOptional.get().setBalance(new Money(savingsOptional.get().getBalance().decreaseAmount(savingsPenalty)));
                savingsRepository.save(savingsOptional.get());
                Transactions penaltyTransaction = new Transactions(savingsOptional.get(),savingsPenalty, TransactionTypes.PENALTY);
                transactionsRepository.save(penaltyTransaction);
            }
        }

    }


}
