package com.ironhack.midtermproject.dao;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "userId")
public class AccountHolder extends User {

    private Date dateOfBirth;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address primaryAddress;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address mailingAddress;


    public AccountHolder(String name, Date dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name);
        setDateOfBirth(dateOfBirth);
        setPrimaryAddress(primaryAddress);
        setMailingAddress(mailingAddress);
    }
}
