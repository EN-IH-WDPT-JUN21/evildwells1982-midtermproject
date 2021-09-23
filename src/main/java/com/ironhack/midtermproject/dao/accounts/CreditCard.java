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
public class CreditCard extends Account {

    @Column(name = "interest_rate", precision = 10, scale = 4, columnDefinition = "DECIMAL(10,4)")
    private BigDecimal interestRate = new BigDecimal(0.2);

    @Embedded
    @AttributeOverride(name="amount", column = @Column(name="limit_amount"))
    @AttributeOverride(name="currency", column = @Column(name="limit_currency"))
    private Money creditLimit = new Money(new BigDecimal(100));

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate, BigDecimal creditLimit) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
        setCreditLimit(creditLimit);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner);
        setInterestRate(interestRate);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, BigDecimal creditLimit, AccountHolder secondaryOwner) {
        super(balance, primaryOwner, secondaryOwner);
        setCreditLimit(creditLimit);
    }

    public CreditCard(Money balance, AccountHolder primaryOwner) {
        super(balance, primaryOwner);
    }

    public void setInterestRate(BigDecimal interestRate){
        if(interestRate.floatValue()<0){
            throw new IllegalArgumentException("Interest Rate cannot be less than zero");
        }
        else if(interestRate.subtract(new BigDecimal(0.2)).floatValue()>0){
            throw new IllegalArgumentException("Interest Rate cannot be greater than 0.2");
        }
        else if(interestRate.subtract(new BigDecimal(0.1)).floatValue()<0){
            throw new IllegalArgumentException("Interest Rate cannot be lower than 0.1");
        }
        else {
            this.interestRate = interestRate;
        }
    }

    public void setCreditLimit(BigDecimal creditLimit){
        if(creditLimit.floatValue()<100){
            throw new IllegalArgumentException("Minimum Balance cannot be less than 100");
        }
        else if(creditLimit.subtract(new BigDecimal(100000)).floatValue()>0){
            throw new IllegalArgumentException("Minimum Balance cannot be greater than 100000");
        }
        else {
            this.creditLimit = new Money(creditLimit);
        }
    }



    public void applyPenalty() {
        //Apply penalty fee if credit card is over limit after a transaction (but under limit before the transaction)
    }


    public void applyInterest() {
        //apply credit card interest based on number of months since interest last applied (should always be called whenever a transaction is made)
    }
}
