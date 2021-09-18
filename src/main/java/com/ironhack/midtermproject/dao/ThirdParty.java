package com.ironhack.midtermproject.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Getter
@Setter
@NoArgsConstructor


@PrimaryKeyJoinColumn(referencedColumnName = "userId")

public class ThirdParty extends Users {

    //change this later
    private final int hashKey = 123456789;

    public ThirdParty(String name) {
        super(name);
    }
}
