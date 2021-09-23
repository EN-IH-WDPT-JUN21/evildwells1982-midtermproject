package com.ironhack.midtermproject.repository;

import com.ironhack.midtermproject.dao.roles.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

}
