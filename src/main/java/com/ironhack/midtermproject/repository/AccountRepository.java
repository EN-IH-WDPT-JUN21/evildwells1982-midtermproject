package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.accounts.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    //Return Account Id and balance for primary or secondary account owner
    @Query(value= "select account_id, balance_currency, balance_amount from account where primary_id = :passedId or secondary_id = :passedId", nativeQuery = true)
    List<Object[]> findAccountsForCustomerId(@Param("passedId") Long passedId);

    @Query(value= "select account_id, balance_currency, balance_amount from account where account_id = :passedId", nativeQuery = true)
    List<Object[]> findAccountsForAccountId(@Param("passedId") Long passedId);

    //Update an Account Creation Date for Testing Only
    @Modifying
    @Query(value="UPDATE account SET creation_date = :creationdate WHERE account_id = :accountid",nativeQuery = true)
    @Transactional
    void updateCreationDate(@Param("creationdate") LocalDate creationDate, @Param("accountid") Long account);
}
