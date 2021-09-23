package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.roles.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


}
