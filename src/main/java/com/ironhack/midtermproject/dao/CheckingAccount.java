package com.ironhack.midtermproject.dao;

import com.ironhack.midtermproject.interfaces.Maintenance;
import com.ironhack.midtermproject.interfaces.Penalties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "accountID")
public class CheckingAccount extends Account {

    private String secretKey;
    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="minimum_balance_amount"))
    @AttributeOverride(name="currency", column = @Column(name="minimum_balance_currency"))
    private final Money minimumBalance = new Money(new BigDecimal(250));;

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="maintenance_amount"))
    @AttributeOverride(name="currency", column = @Column(name="maintenance_currency"))
    private final Money monthlyMaintenanceFee = new Money(new BigDecimal(12));;

    public CheckingAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);

    }

    public CheckingAccount(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);

    }


    public void applyPenalty(){
        //placeholder for applying penalty fee method should be called on every account movement,
        // and should deduct the penalty if the previous balance was greater than the minimum balance,
        // and the new balance is less than the minimum balance

    }

    public void applyMaintenance(){
        //placeholder for applying Maintenance Fee will need to access the number of months since the account was last accessed,
        // and apply maintenance fee for each month elapsed, then call applyPenalty method
    }

}
