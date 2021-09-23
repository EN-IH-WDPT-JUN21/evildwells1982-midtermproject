package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.roles.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
