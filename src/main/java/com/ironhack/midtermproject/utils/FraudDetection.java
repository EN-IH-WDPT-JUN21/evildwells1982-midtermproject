package com.ironhack.midtermproject.utils;

import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.dao.accounts.Transactions;
import com.ironhack.midtermproject.enums.AccountStatus;
import com.ironhack.midtermproject.repository.AccountRepository;
import com.ironhack.midtermproject.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FraudDetection {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    public Transactions checkTransactionsInOneSecond(Long inAccount, Long outAccount, LocalDateTime transactionDateTime){

        if(outAccount!=null) {
            Optional<Integer> transactionsInOneSecondSending = transactionsRepository.findTransactionsInOneSecond(outAccount, transactionDateTime);
            if (transactionsInOneSecondSending.isPresent() && transactionsInOneSecondSending.get() >= 2) {
                Account outgoingAccount = accountRepository.getById(outAccount);
                outgoingAccount.setAccountStatus(AccountStatus.FROZEN);
                accountRepository.save(outgoingAccount);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud Detection, Origin Account Frozen");
            }
        }

        if(inAccount!=null) {
            Optional<Integer> transactionsInOneSecondReceiving = transactionsRepository.findTransactionsInOneSecond(inAccount,transactionDateTime);
            if (transactionsInOneSecondReceiving.isPresent() && transactionsInOneSecondReceiving.get() >= 2) {
                Account incomingAccount = accountRepository.getById(inAccount);
                incomingAccount.setAccountStatus(AccountStatus.FROZEN);
                accountRepository.save(incomingAccount);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud Detection, Destination Account Frozen");
            }
        }

        return null;
    }

    public Transactions checkMaxTransactionsIn24Hours(Long inAccount, Long outAccount, LocalDateTime transactionDateTime, BigDecimal transactionAmount){

        if(outAccount!=null) {
            Optional<BigDecimal> maxTransactionsin24 = transactionsRepository.findMaxTransactionIn24Hours(outAccount);
            Optional<BigDecimal> transactionsInLast24 = transactionsRepository.findSumTransactionsIn24Hours(outAccount, transactionDateTime);
            if (maxTransactionsin24.isPresent()) {

                if(transactionsInLast24.isPresent()){
                    BigDecimal twentyFourHours = transactionsInLast24.get();
                    twentyFourHours = twentyFourHours.add(transactionAmount);


                    if(twentyFourHours.compareTo(maxTransactionsin24.get().multiply(new BigDecimal("1.5")))>0){
                        Account outgoingAccount = accountRepository.getById(outAccount);
                        outgoingAccount.setAccountStatus(AccountStatus.FROZEN);
                        accountRepository.save(outgoingAccount);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud Detection, Origin Account Frozen");
                    }

                } else{
                    if(transactionAmount.compareTo(maxTransactionsin24.get().multiply(new BigDecimal("1.5")))>0){
                        Account outgoingAccount = accountRepository.getById(outAccount);
                        outgoingAccount.setAccountStatus(AccountStatus.FROZEN);
                        accountRepository.save(outgoingAccount);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud Detection, Origin Account Frozen");
                    }
                }
            }
        }


        if(inAccount!=null) {
            Optional<BigDecimal> maxTransactionsin24 = transactionsRepository.findMaxTransactionIn24Hours(inAccount);
            Optional<BigDecimal> transactionsInLast24 = transactionsRepository.findSumTransactionsIn24Hours(inAccount, transactionDateTime);
            if (maxTransactionsin24.isPresent()) {

                if(transactionsInLast24.isPresent()){
                    BigDecimal twentyFourHours = transactionsInLast24.get();
                    twentyFourHours = twentyFourHours.add(transactionAmount);


                    if(twentyFourHours.compareTo(maxTransactionsin24.get().multiply(new BigDecimal("1.5")))>0){
                        Account incomingAccount = accountRepository.getById(inAccount);
                        incomingAccount.setAccountStatus(AccountStatus.FROZEN);
                        accountRepository.save(incomingAccount);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud Detection, Origin Account Frozen");
                    }

                } else{
                    if(transactionAmount.compareTo(maxTransactionsin24.get().multiply(new BigDecimal("1.5")))>0){
                        Account incomingAccount = accountRepository.getById(inAccount);
                        incomingAccount.setAccountStatus(AccountStatus.FROZEN);
                        accountRepository.save(incomingAccount);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud Detection, Origin Account Frozen");
                    }
                }
            }
        }

        return null;
    }



}
