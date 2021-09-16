package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.enums.AccountStatus;
import com.ironhack.midtermproject.interfaces.Penalties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Account implements Penalties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="balance_amount"))
    @AttributeOverride(name="currency", column = @Column(name="balance_currency"))
    private Money balance;

    @ManyToOne
    @JoinColumn(name = "primaryId")
    private AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondaryId")
    private AccountHolder secondaryOwner;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="penalty_amount"))
    @AttributeOverride(name="currency", column = @Column(name="penalty_currency"))
    private final Money penaltyFee = new Money(new BigDecimal(40));

    private final Date creationDate = new Date();


    @OneToMany(mappedBy = "sourceAccount")
    private List<Transactions> transactionOut;

    @OneToMany(mappedBy = "destinationAccount")
    private List<Transactions> transactionIn;


    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }


}
