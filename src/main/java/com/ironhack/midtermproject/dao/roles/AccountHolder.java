package com.ironhack.midtermproject.dao.roles;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.midtermproject.dao.accounts.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "userId", name="holderId")
public class AccountHolder extends Users {

    private LocalDate dateOfBirth;

    @JsonManagedReference(value = "primary-address")
    @OneToOne
    @JoinColumn(name = "primary_address_id")
    private Address primaryAddress;

    @JsonManagedReference(value = "mailing-address")
    @OneToOne
    @JoinColumn(name = "mailing_address_id")
    private Address mailingAddress;

    @JsonManagedReference(value = "primary-owner")
    @OneToMany(mappedBy = "primaryOwner")
    private List<Account> primaryAccountList;

    @JsonManagedReference(value = "secondary-owner")
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
