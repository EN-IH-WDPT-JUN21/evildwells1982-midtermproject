package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

}
