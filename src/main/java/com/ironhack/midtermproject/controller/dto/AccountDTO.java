package com.ironhack.midtermproject.controller.dto;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    @NotNull
    @Digits(integer=10,fraction = 2,message="Wrong balance format")
    private BigDecimal balance;


    private String secretKey;

    @NotNull
    private Long primaryId;

    private Long secondaryId;

    private BigDecimal interestRate;

    private BigDecimal minimumBalance;

    private BigDecimal creditLimit;


    public AccountDTO(BigDecimal balance, String secretKey, Long primaryId) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.primaryId = primaryId;

    }

    public AccountDTO(BigDecimal balance, Long primaryId) {
        this.balance = balance;
        this.primaryId = primaryId;
    }
}
