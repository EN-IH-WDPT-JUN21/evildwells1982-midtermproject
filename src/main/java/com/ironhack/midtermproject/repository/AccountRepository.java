package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    //Return Account Id and balance for primary or secondary account owner
    @Query(value= "select account_id, balance_currency, balance_amount from account where primary_id = :passedId or secondary_id = :passedId", nativeQuery = true)
    List<Object[]> findAccountsForCustomerId(@Param("passedId") Long passedId);

}
