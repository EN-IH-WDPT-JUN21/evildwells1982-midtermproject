package com.ironhack.midtermproject.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@PrimaryKeyJoinColumn(referencedColumnName = "userId")

public class ThirdParty extends User{

    private final int hashKey = getName().hashCode();

    public ThirdParty(String name) {
        super(name);
    }
}
