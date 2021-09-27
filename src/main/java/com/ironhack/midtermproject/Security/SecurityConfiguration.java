package com.ironhack.midtermproject.Security;

import com.ironhack.midtermproject.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic(); // Use Basic Authentication
        http.csrf().disable(); // Disables csrf prevents calling different page from within the existing page (or something)
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/accounts/**").hasAnyRole("Account_Holder")
                .mvcMatchers(HttpMethod.GET, "/myaccounts").hasAnyRole("Account_Holder")
                .mvcMatchers(HttpMethod.GET,"/account/**").hasAnyRole("Admin")
                .mvcMatchers(HttpMethod.GET,"/newaccount/**").hasAnyRole("Admin")
                .mvcMatchers(HttpMethod.POST,"/newthirdparty").hasAnyRole("Admin")
                .mvcMatchers(HttpMethod.PATCH,"/updatebalance/**").hasAnyRole("Admin")
                .mvcMatchers(HttpMethod.POST, "/transferfunds").hasAnyRole("Account_Holder")
                .mvcMatchers(HttpMethod.POST, "/sendfunds").hasAnyRole("Third_Party")
                .mvcMatchers(HttpMethod.POST, "/claimfunds").hasAnyRole("Third_Party")
                .anyRequest().permitAll();
    }





}
