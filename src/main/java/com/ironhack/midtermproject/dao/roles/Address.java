package com.ironhack.midtermproject.dao.roles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ironhack.midtermproject.dao.roles.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AddressId;

    @NotBlank(message = "You must provide a street address")
    private String street;

    @NotBlank(message = "You must provide a Postcode")
    private String postcode;

    @JsonBackReference
    @OneToOne(mappedBy = "primaryAddress")
    private AccountHolder primaryHolder;

    @JsonBackReference
    @OneToOne(mappedBy = "mailingAddress")
    private AccountHolder mailingHolder;


    public Address(String street, String postcode) {
        this.street = street;
        this.postcode = postcode;
    }
}
