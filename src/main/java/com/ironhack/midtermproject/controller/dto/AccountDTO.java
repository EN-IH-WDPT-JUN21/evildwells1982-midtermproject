package com.ironhack.midtermproject.controller.dto;

import com.ironhack.midtermproject.dao.roles.AccountHolder;
import com.ironhack.midtermproject.utils.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private BigDecimal balance;

    private String secretKey;

    private Long primaryId;

    private Long secondaryId;

    private BigDecimal interestRate;

    private Money creditLimit;


}
