package com.ironhack.midtermproject.dao.accounts;

import com.ironhack.midtermproject.utils.Money;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "accountID")
public class Savings extends Account {

    @Column(name = "interest_rate", precision = 10, scale = 4, columnDefinition = "DECIMAL(10,4)")
    private BigDecimal interestRate = new BigDecimal("0.0025");
    private String secretKey;

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="minimum_balance_amount"))
    @AttributeOverride(name="currency", column = @Column(name="minimum_balance_currency"))
    private Money minimumBalance = new Money(new BigDecimal(1000));


    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setSecretKey(secretKey);
    }

    public Savings(Money balance, AccountHolder primaryOwner, BigDecimal interestRate, String secretKey) {
        super(balance, primaryOwner);
        setInterestRate(interestRate);
        setSecretKey(secretKey);
    }

    public Savings(Money balance, AccountHolder primaryOwner, BigDecimal interestRate, String secretKey, BigDecimal minimumBalance) {
        super(balance, primaryOwner);
        setInterestRate(interestRate);
        setSecretKey(secretKey);
        setMinimumBalance(minimumBalance);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal minimumBalance) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
        setMinimumBalance(minimumBalance);
    }

    public Savings(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, String secretKey, BigDecimal minimumBalance) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setSecretKey(secretKey);
        setMinimumBalance(minimumBalance);
    }

    public Savings(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
    }
    public Savings(Money balance, AccountHolder primaryOwner, String secretKey, BigDecimal minimumBalance) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
    }



    public void setInterestRate(BigDecimal interestRate){
        if(interestRate.floatValue()<0){
            throw new IllegalArgumentException("Interest Rate cannot be less than zero");
        }
        else if(interestRate.subtract(new BigDecimal(0.5)).floatValue()>0){
            throw new IllegalArgumentException("Interest Rate cannot be greater than 0.5");
        }
        else {
            this.interestRate = interestRate;
        }
    }

    public void setMinimumBalance(BigDecimal minimumBalance){
        if(minimumBalance.floatValue()<100){
            throw new IllegalArgumentException("Minimum Balance cannot be less than 100");
        }
        else if(minimumBalance.subtract(new BigDecimal(1000)).floatValue()>0){
            throw new IllegalArgumentException("Minimum Balance cannot be greater than 1000");
        }
        else {
            this.minimumBalance = new Money(minimumBalance);
        }
    }


    public void applyPenalty() {
        //no minimum balance for a savings account so does not apply
    }


    public void applyInterest() {
        //for applying interest to the savings account
    }
}
