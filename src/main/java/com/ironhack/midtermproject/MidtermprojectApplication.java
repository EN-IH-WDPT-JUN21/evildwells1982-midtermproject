package com.ironhack.midtermproject;

import com.ironhack.midtermproject.dao.AccountHolder;
import com.ironhack.midtermproject.dao.Address;
import com.ironhack.midtermproject.dao.CheckingAccount;
import com.ironhack.midtermproject.dao.Money;
import com.ironhack.midtermproject.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


@SpringBootApplication
public class MidtermprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidtermprojectApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(AccountRepository accountRepository, CheckingAccountRepository checkingAccountRepository, UserRepository userRepository, AccountHolderRepository accountHolderRepository, AddressRepository addressRepository){
//		return args -> {
//			Address address1 = new Address("Test Street","Test Postcode");
//			addressRepository.save(address1);
//			AccountHolder holder1 = new AccountHolder("Test Customer",LocalDate.of(2014,2,11),address1);
//			accountHolderRepository.save(holder1);
//			CheckingAccount checking1 = new CheckingAccount(new Money(new BigDecimal(500)),holder1,"SomeSecretKey");
//			checkingAccountRepository.save(checking1);
//
//		};
//	}
//
//	@Override
//	public void run(String...args){
//	}

}
