package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.Transactions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    //Find Date of last Maintenance Fee
    @Query(value = "select MAX(transaction_date_time) FROM transactions WHERE source_account = :account AND transaction_type = 'MAINTENANCE'", nativeQuery = true)
    Optional<LocalDateTime> findLastMaintenance(@Param("account") Long account);

    //Find Date of last Interest Payment
    @Query(value = "select MAX(transaction_date_time) FROM transactions WHERE destination_account = :account AND transaction_type = 'INTEREST'", nativeQuery = true)
    Optional<LocalDateTime> findLastInterest(@Param("account") Long account);

}
