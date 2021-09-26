package com.ironhack.midtermproject.dao.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


@PrimaryKeyJoinColumn(referencedColumnName = "userId")

public class ThirdParty extends Users {

    //change this later
    @NotBlank(message = "Third Parties must have a hashkey")
    private String hashKey;

    public ThirdParty(String name, String hashKey) {
        super(name,"Third_Party");
        setHashKey(hashKey);
    }
}
