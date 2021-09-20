package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.Transactions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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


    //Insert a Transaction (for Testing purposes only)
    @Modifying
    @Query(value="INSERT INTO transactions (destination_account, amount, currency, transaction_date_time, transaction_type) VALUES (:account, 500, 'USD', '2020-09-19 22:41:52.171718', 'INTEREST')", nativeQuery = true)
    @Transactional
    void insertInterestTransactionTesting(@Param("account") Long account);

    //Update a transaction Date (Testing Only)
    @Modifying
    @Query(value="UPDATE transactions SET transaction_date_time = :transactiondate WHERE transaction_id = :transactionid",nativeQuery = true)
    @Transactional
    void updateTransactionDate(@Param("transactiondate") LocalDateTime transactionDate, @Param("transactionid") Long transaction);



}
