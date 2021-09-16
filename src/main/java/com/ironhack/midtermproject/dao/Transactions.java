package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.enums.TransactionTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "sourceAccount")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destinationAccount")
    private Account destinationAccount;

    @Embedded
    private Money transactionAmount;

    private final LocalDateTime transactionDateTime = LocalDateTime.now();

    @Enumerated
    private TransactionTypes transactionType;

    @ManyToOne
    @JoinColumn(name = "performedBy")
    private User performedBy;


    //Constructor for Customers Paying Customers, and admins transferring between accounts
    public Transactions(Account sourceAccount, Account destinationAccount, Money transactionAmount, User performedBy) {
        setSourceAccount(sourceAccount);
        setDestinationAccount(destinationAccount);
        setTransactionAmount(transactionAmount);
        setTransactionType(TransactionTypes.ACCOUNTTOACCOUNT);;
        setPerformedBy(performedBy);
    }

    //Constructor for paying out to third parties, and for Admins removing money from accounts
    public Transactions(Account sourceAccount, Money transactionAmount, TransactionTypes transactionType, User performedBy) {
        setSourceAccount(sourceAccount);
        setTransactionAmount(transactionAmount);
        setTransactionType(transactionType);
        setPerformedBy(performedBy);
    }

    //Constructor for money in from third parties, and for Admins adding money to accounts
    public Transactions(Money transactionAmount, Account destinationAccount, TransactionTypes transactionType, User performedBy) {
        setDestinationAccount(destinationAccount);
        setTransactionAmount(transactionAmount);
        setTransactionType(transactionType);
        setPerformedBy(performedBy);
    }

    //Constructor for Interest adding to account
    public Transactions(Account destinationAccount, Money transactionAmount) {
        setDestinationAccount(destinationAccount);
        setTransactionAmount(transactionAmount);
        setTransactionType(TransactionTypes.INTEREST);
    }

    //constructor for interest, fees etc going from account
    public Transactions(Account sourceAccount, Money transactionAmount, TransactionTypes transactionType) {
        setSourceAccount(sourceAccount);
        setTransactionAmount(transactionAmount);
        setTransactionType(transactionType);
    }
}
