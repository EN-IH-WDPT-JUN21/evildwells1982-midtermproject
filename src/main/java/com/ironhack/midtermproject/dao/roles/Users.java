package com.ironhack.midtermproject.dao.roles;

import com.ironhack.midtermproject.dao.accounts.Transactions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "All Users Must Have a Name")
    private String name;

    @OneToMany(mappedBy = "performedBy")
    private List<Transactions> transactionList;

    public Users(String name) {
        setName(name);
    }
}
