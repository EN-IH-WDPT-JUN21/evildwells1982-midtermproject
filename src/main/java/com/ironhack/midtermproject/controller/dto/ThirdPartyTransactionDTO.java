package com.ironhack.midtermproject.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ThirdPartyTransactionDTO {

    @NotNull(message = "You must provide an amount")
    private BigDecimal amount;

    @NotNull(message = "You must provide an accountID")
    private Long accountId;

    @NotBlank(message = "You must provide the secretKey")
    private String secretKey;


}
