package com.ironhack.midtermproject.dao;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "userId")
public class AccountHolder extends Users {

    private LocalDate dateOfBirth;

    @OneToOne
    @JoinColumn(name = "primary_address_id")
    private Address primaryAddress;

    @OneToOne
    @JoinColumn(name = "mailing_address_id")
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner")
    private List<Account> primaryAccountList;

    @OneToMany(mappedBy = "secondaryOwner")
    private List<Account> secondaryAccountList;



    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name);
        setDateOfBirth(dateOfBirth);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(mailingAddress);
    }

    public AccountHolder(String name, LocalDate dateOfBirth, Address primaryAddress) {
        super(name);
        setDateOfBirth(dateOfBirth);
        setPrimaryAddress(primaryAddress);

    }
}
