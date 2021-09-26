package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.dto.BalanceDTO;
import com.ironhack.midtermproject.controller.dto.ThirdPartyTransactionDTO;
import com.ironhack.midtermproject.controller.dto.TransferDTO;
import com.ironhack.midtermproject.controller.interfaces.ITransactionController;
import com.ironhack.midtermproject.dao.accounts.*;
import com.ironhack.midtermproject.enums.TransactionTypes;
import com.ironhack.midtermproject.repository.*;
import com.ironhack.midtermproject.utils.AccountUtility;
import com.ironhack.midtermproject.utils.Money;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController implements ITransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Autowired
    private SavingsRepository savingsRepository;

    @Autowired
    private StudentCheckingRepository studentCheckingRepository;

    @Autowired
    private AccountUtility accountUtility;

    @Autowired
    private UserRepository userRepository;

    @PatchMapping("/updatebalance/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Transactions updateBalance(@PathVariable(name= "accountId") Long accountId, @RequestBody @Valid BalanceDTO balanceDTO){

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

            //***Needs to be updated to take userId of admin user executing request***
            Transactions adminTransaction = new Transactions(balanceDifference,account.get(),transactionTypes, userRepository.getById(1L));
            accountRepository.save(account.get());
            return transactionsRepository.save(adminTransaction);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Account for that reference");
        }
    }

    @PostMapping("/transferfunds/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Transactions transferFunds(@PathVariable(name = "accountId") Long sourceAccount, @RequestBody @Valid TransferDTO transferDTO) {

        Optional<Account> ownedAccount = accountRepository.findById(sourceAccount);
        Optional<Account> receivingAccount = accountRepository.findById(transferDTO.getDestinationAccount());
        Optional<CreditCard> isCreditCard = creditCardRepository.findById(sourceAccount);
        Money newBalance;

        //**Needs to be updated to check that account belongs to individual that is sending the request***

        // Condition checking to ensure valid request
        if (ownedAccount.isPresent()) {
            List<Object[]> list = accountRepository.findAccountsForAccountId(sourceAccount);

            //apply Interest, Maintenance Fees, and account Penalties before checking available balance.
            for (Object[] result : list) {
                accountUtility.applyInterest(((BigInteger)result[0]).longValue());
                accountUtility.applyMaintenance(((BigInteger)result[0]).longValue());
                accountUtility.applyPenalty(((BigInteger)result[0]).longValue(),new Money((BigDecimal)result[2]));
            }
            newBalance = accountRepository.findById(sourceAccount).get().getBalance();
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Owned Account for that reference");
        }
            //check credit card available funds
        if(isCreditCard.isPresent()){
            if((isCreditCard.get().getCreditLimit().decreaseAmount(newBalance)).compareTo(transferDTO.getTransferAmount())<0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient funds available");
            }
        } else {
            if (newBalance.getAmount().compareTo(transferDTO.getTransferAmount()) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Insufficient funds available");
            }
        }

        if(receivingAccount.isPresent()){
            if(!receivingAccount.get().getPrimaryOwner().getName().toUpperCase().equals(transferDTO.getHolderName().toUpperCase()) && (receivingAccount.get().getSecondaryOwner() == null ||   !receivingAccount.get().getSecondaryOwner().getName().toUpperCase().equals(transferDTO.getHolderName().toUpperCase()))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Account Owner Name does not match");
            }
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No Destination Account for that reference");
        }

        // Actual transaction
        Account outAccount = accountRepository.getById(sourceAccount);
        Account inAccount = accountRepository.getById(transferDTO.getDestinationAccount());

        outAccount.setBalance(new Money(outAccount.getBalance().decreaseAmount(transferDTO.getTransferAmount())));
        accountRepository.save(outAccount);

        inAccount.setBalance(new Money(inAccount.getBalance().increaseAmount(transferDTO.getTransferAmount())));
        accountRepository.save(inAccount);

        Transactions transferTransaction = new Transactions(outAccount,inAccount,new Money(transferDTO.getTransferAmount()), outAccount.getPrimaryOwner());
        return transactionsRepository.save(transferTransaction);

    }

    @PostMapping("/sendfunds")
    @ResponseStatus(HttpStatus.CREATED)
    public Transactions sendFunds(@RequestHeader("hashkey") String hashKey, @RequestBody @Valid ThirdPartyTransactionDTO thirdPartyTransactionDTO){

        Optional<Account> destinationAccount = accountRepository.findById(thirdPartyTransactionDTO.getAccountId());

        Optional<Savings> savings = savingsRepository.findById(thirdPartyTransactionDTO.getAccountId());
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(thirdPartyTransactionDTO.getAccountId());
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(thirdPartyTransactionDTO.getAccountId());
        String secretKey = "temp";

        //***Need to update this to check the hashkey for the specific third party user***

        if(!hashKey.equals("T3stTh4rd")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"HashKey does not match");
        }

        if(destinationAccount.isPresent()){

            List<Object[]> list = accountRepository.findAccountsForAccountId(thirdPartyTransactionDTO.getAccountId());

            //apply Interest, Maintenance Fees, and account Penalties before adding more funds to balance.
            for (Object[] result : list) {
                accountUtility.applyInterest(((BigInteger)result[0]).longValue());
                accountUtility.applyMaintenance(((BigInteger)result[0]).longValue());
                accountUtility.applyPenalty(((BigInteger)result[0]).longValue(),new Money((BigDecimal)result[2]));
            }
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No account for that ID");
        }


        if(checkingAccount.isPresent()){
            secretKey = checkingAccount.get().getSecretKey();
        }else if(savings.isPresent()){
            secretKey = savings.get().getSecretKey();
        }else if(studentChecking.isPresent()){
            secretKey = studentChecking.get().getSecretKey();
        }

        if(!secretKey.equals(thirdPartyTransactionDTO.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"SecretKey does not match");
        }

        // Actual transaction
        Account inAccount = accountRepository.getById(thirdPartyTransactionDTO.getAccountId());

        inAccount.setBalance(new Money(inAccount.getBalance().increaseAmount(thirdPartyTransactionDTO.getAmount())));
        accountRepository.save(inAccount);

        //change this to take actual third party id
        Transactions thirdPartyTransaction = new Transactions(new Money(thirdPartyTransactionDTO.getAmount()),inAccount, TransactionTypes.THIRDPARTYTOACCOUNT, userRepository.getById(4L));
        return transactionsRepository.save(thirdPartyTransaction);


    }


    @PostMapping("/claimfunds")
    @ResponseStatus(HttpStatus.CREATED)
    public Transactions claimFunds(@RequestHeader("hashkey") String hashKey, @RequestBody @Valid ThirdPartyTransactionDTO thirdPartyTransactionDTO){

        Optional<Account> destinationAccount = accountRepository.findById(thirdPartyTransactionDTO.getAccountId());

        Optional<Savings> savings = savingsRepository.findById(thirdPartyTransactionDTO.getAccountId());
        Optional<CheckingAccount> checkingAccount = checkingAccountRepository.findById(thirdPartyTransactionDTO.getAccountId());
        Optional<StudentChecking> studentChecking = studentCheckingRepository.findById(thirdPartyTransactionDTO.getAccountId());
        String secretKey = "temp";

        //***Need to update this to check the hashkey for the specific third party user***

        if(!hashKey.equals("T3stTh4rd")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"HashKey does not match");
        }

        if(destinationAccount.isPresent()){

            List<Object[]> list = accountRepository.findAccountsForAccountId(thirdPartyTransactionDTO.getAccountId());

            //apply Interest, Maintenance Fees, and account Penalties before adding more funds to balance.
            for (Object[] result : list) {
                accountUtility.applyInterest(((BigInteger)result[0]).longValue());
                accountUtility.applyMaintenance(((BigInteger)result[0]).longValue());
                accountUtility.applyPenalty(((BigInteger)result[0]).longValue(),new Money((BigDecimal)result[2]));
            }
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"No account for that ID");
        }


        if(checkingAccount.isPresent()){
            secretKey = checkingAccount.get().getSecretKey();
        }else if(savings.isPresent()){
            secretKey = savings.get().getSecretKey();
        }else if(studentChecking.isPresent()){
            secretKey = studentChecking.get().getSecretKey();
        }

        if(!secretKey.equals(thirdPartyTransactionDTO.getSecretKey())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"SecretKey does not match");
        }

        // Actual transaction
        Account outAccount = accountRepository.getById(thirdPartyTransactionDTO.getAccountId());

        outAccount.setBalance(new Money(outAccount.getBalance().decreaseAmount(thirdPartyTransactionDTO.getAmount())));
        accountRepository.save(outAccount);

        //change this to take actual third party id
        Transactions thirdPartyTransaction = new Transactions(outAccount,new Money(thirdPartyTransactionDTO.getAmount().multiply(new BigDecimal(-1))), TransactionTypes.ACCOUNTTOTHIRDPARTY, userRepository.getById(4L));
        return transactionsRepository.save(thirdPartyTransaction);


    }


}
