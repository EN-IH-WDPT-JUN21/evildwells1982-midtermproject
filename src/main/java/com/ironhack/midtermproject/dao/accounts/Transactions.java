package com.ironhack.midtermproject.dao.accounts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.dao.roles.Users;
import com.ironhack.midtermproject.enums.TransactionTypes;
import com.ironhack.midtermproject.utils.Money;
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

    @JsonBackReference(value = "source_account")
    @ManyToOne
    @JoinColumn(name = "sourceAccount")
    private Account sourceAccount;

    @JsonBackReference(value = "destination_account")
    @ManyToOne
    @JoinColumn(name = "destinationAccount")
    private Account destinationAccount;

    @Embedded
    private Money transactionAmount;

    private final LocalDateTime transactionDateTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private TransactionTypes transactionType;

    @JsonBackReference(value = "performed_by")
    @ManyToOne
    @JoinColumn(name = "performedBy")
    private Users performedBy;


    //Constructor for Customers Paying Customers, and admins transferring between accounts
    public Transactions(Account sourceAccount, Account destinationAccount, Money transactionAmount, Users performedBy) {
        setSourceAccount(sourceAccount);
        setDestinationAccount(destinationAccount);
        setTransactionAmount(transactionAmount);
        setTransactionType(TransactionTypes.ACCOUNTTOACCOUNT);;
        setPerformedBy(performedBy);
    }

    //Constructor for paying out to third parties, and for Admins removing money from accounts
    public Transactions(Account sourceAccount, Money transactionAmount, TransactionTypes transactionType, Users performedBy) {
        setSourceAccount(sourceAccount);
        setTransactionAmount(transactionAmount);
        setTransactionType(transactionType);
        setPerformedBy(performedBy);
    }

    //Constructor for money in from third parties, and for Admins adding money to accounts
    public Transactions(Money transactionAmount, Account destinationAccount, TransactionTypes transactionType, Users performedBy) {
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
