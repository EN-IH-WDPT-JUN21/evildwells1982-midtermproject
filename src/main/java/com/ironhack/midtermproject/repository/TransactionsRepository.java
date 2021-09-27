package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.accounts.Transactions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    //Find Date of last Maintenance Fee
    @Query(value = "select MAX(transaction_date_time) FROM transactions WHERE source_account = :account AND transaction_type = 'MAINTENANCE'", nativeQuery = true)
    Optional<LocalDateTime> findLastMaintenance(@Param("account") Long account);

    //Find Date of last Interest Payment
    @Query(value = "select MAX(transaction_date_time) FROM transactions WHERE destination_account = :account AND transaction_type = 'INTEREST'", nativeQuery = true)
    Optional<LocalDateTime> findLastInterest(@Param("account") Long account);

    //Transactions in 1 second
    @Query(value = "select count(transaction_id) FROM transactions WHERE (destination_account = :account OR source_account = :account) " +
            "AND transaction_type not in ('INTEREST','MAINTENANCE', 'PENALTY', 'ADMINTOACCOUNT', 'ADMINFROMACCOUNT') AND transaction_date_time >= (:transactiondate - INTERVAL 1 SECOND)", nativeQuery = true)
    Optional<Integer> findTransactionsInOneSecond(@Param("account") Long account, @Param("transactiondate") LocalDateTime transactionDate);

    //Maximum Transaction Value in rolling 24 hours
    @Query(value ="select max(sumtransactions) from( " +
            "select a.transaction_id, sum(b.amount) as sumtransactions from transactions a left join transactions b " +
            "on (a.source_account = b.source_account or a.source_account = b.destination_account or a.destination_account = b.destination_account or a.destination_account = b.source_account) " +
            "and a.transaction_date_time - INTERVAL 24 HOUR < b.transaction_date_time and a.transaction_date_time >= b.transaction_date_time " +
            "where a.transaction_type not in ('INTEREST','MAINTENANCE', 'PENALTY', 'ADMINTOACCOUNT', 'ADMINFROMACCOUNT') " +
            "and b.transaction_type not in ('INTEREST','MAINTENANCE', 'PENALTY', 'ADMINTOACCOUNT', 'ADMINFROMACCOUNT') " +
            "and (a.source_account = :account OR a.destination_account = :account) " +
            "group by a.transaction_id " +
            ") c", nativeQuery = true)
    Optional<BigDecimal> findMaxTransactionIn24Hours(@Param("account") Long account);

    //Transactions in most recent 24hours
    @Query(value = "select sum(amount) FROM transactions WHERE (destination_account = :account OR source_account = :account) " +
            "AND transaction_type not in ('INTEREST','MAINTENANCE', 'PENALTY', 'ADMINTOACCOUNT', 'ADMINFROMACCOUNT') AND transaction_date_time >= (:transactiondate - INTERVAL 24 HOUR)", nativeQuery = true)
    Optional<BigDecimal> findSumTransactionsIn24Hours(@Param("account") Long account, @Param("transactiondate") LocalDateTime transactionDate);




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
