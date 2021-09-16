package com.ironhack.midtermproject.dao;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "sourceAccount")
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destinationAccount")
    private Account destinationAccount;

    @Embedded
    private Money transactionAmount;

    private final LocalDateTime transactionDateTime = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "performedBy")
    private User performedBy;



}
