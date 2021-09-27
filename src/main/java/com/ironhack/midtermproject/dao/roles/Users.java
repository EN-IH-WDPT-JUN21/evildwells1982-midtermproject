package com.ironhack.midtermproject.dao.roles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ironhack.midtermproject.dao.accounts.Transactions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Random;

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

    @JsonManagedReference(value = "performed_by")
    @OneToMany(mappedBy = "performedBy")
    private List<Transactions> transactionList;

    private String username;

    private String password;

    private String roles;


    public Users(String name, String roles) {
        setName(name);
        setRoles(roles);
        Random rand = new Random();
        int int_random = rand.nextInt(100);
        String randValue = String.valueOf(int_random);
        setUsername(name.replaceAll("\\s","") + randValue);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        setPassword(passwordEncoder.encode(name.replaceAll("\\s","")));
    }
}
