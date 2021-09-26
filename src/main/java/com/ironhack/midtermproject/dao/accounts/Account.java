package com.ironhack.midtermproject.dao.accounts;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.utils.Money;
import com.ironhack.midtermproject.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Account{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="balance_amount"))
    @AttributeOverride(name="currency", column = @Column(name="balance_currency"))
    private Money balance;

    @JsonBackReference(value = "primary-owner")
    @ManyToOne
    @JoinColumn(name = "primaryId")
    private AccountHolder primaryOwner;

    @JsonBackReference(value = "secondary-owner")
    @ManyToOne
    @JoinColumn(name = "secondaryId")
    private AccountHolder secondaryOwner;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="penalty_amount"))
    @AttributeOverride(name="currency", column = @Column(name="penalty_currency"))
    private final Money penaltyFee = new Money(new BigDecimal(40));

    private final LocalDate creationDate = LocalDate.now();

    @JsonManagedReference(value = "source_account")
    @OneToMany(mappedBy = "sourceAccount")
    private List<Transactions> transactionOut;

    @JsonManagedReference(value = "destination_account")
    @OneToMany(mappedBy = "destinationAccount")
    private List<Transactions> transactionIn;


    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);
        setSecondaryOwner(secondaryOwner);
    }

    public Account(Money balance, AccountHolder primaryOwner) {
        setBalance(balance);
        setPrimaryOwner(primaryOwner);

    }


}
