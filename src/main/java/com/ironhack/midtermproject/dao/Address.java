package com.ironhack.midtermproject.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AddressId;

    private String street;
    private String postcode;

    @OneToOne(mappedBy = "primaryAddress")
    private AccountHolder primaryHolder;

    @OneToOne(mappedBy = "mailingAddress")
    private AccountHolder mailingHolder;


    public Address(String street, String postcode) {
        this.street = street;
        this.postcode = postcode;
    }
}
