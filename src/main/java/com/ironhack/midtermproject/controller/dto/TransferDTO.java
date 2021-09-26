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
public class TransferDTO {

    @NotNull
    private Long destinationAccount;

    @NotBlank
    private String holderName;

    @NotNull
    private BigDecimal transferAmount;




}
