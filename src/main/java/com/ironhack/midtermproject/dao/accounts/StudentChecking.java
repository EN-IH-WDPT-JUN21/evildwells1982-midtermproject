package com.ironhack.midtermproject.dao.accounts;

import com.ironhack.midtermproject.dao.accounts.Account;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "accountID")
public class StudentChecking extends Account {

    private String secretKey;


    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner);
        setSecretKey(secretKey);
    }

    public StudentChecking(Money balance, AccountHolder primaryOwner, String secretKey) {
        super(balance, primaryOwner);
        setSecretKey(secretKey);
    }


    public void applyPenalty() {
        //Student Accounts do not have a minimum balance, so a penalty fee does not apply
    }
}
