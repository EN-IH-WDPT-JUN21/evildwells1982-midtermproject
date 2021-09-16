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
public class CheckingAccount extends Account implements Maintenance, Penalties {

    private String secretKey;
    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="minimum_balance_amount"))
    @AttributeOverride(name="currency", column = @Column(name="minimum_balance_currency"))
    private Money minimumBalance;

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="maintenance_amount"))
    @AttributeOverride(name="currency", column = @Column(name="maintenance_currency"))
    private Money monthlyMaintenanceFee;

    public CheckingAccount(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, Money minimumBalance, Money monthlyMaintenanceFee) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
        this.minimumBalance = new Money(new BigDecimal(250));
        this.monthlyMaintenanceFee = new Money(new BigDecimal(12));
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
