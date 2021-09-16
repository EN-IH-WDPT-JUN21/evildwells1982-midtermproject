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

    private Money balance;

    @OneToOne
    @JoinColumn(name = "userId")
    private AccountHolder primaryOwner;

    @OneToOne
    @JoinColumn(name = "userId")
    private AccountHolder secondaryOwner;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    private final Money penaltyFee = new Money(new BigDecimal(40));

    private final Date creationDate = new Date();


    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }


}
