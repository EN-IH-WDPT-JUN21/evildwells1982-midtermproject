package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.Savings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {

}
