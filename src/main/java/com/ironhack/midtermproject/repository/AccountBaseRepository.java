package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AccountBaseRepository <T extends Account>  extends JpaRepository<T, Long> {

    //To be used for common account repository queries

}
