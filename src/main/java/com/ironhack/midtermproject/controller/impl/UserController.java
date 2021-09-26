package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.IUserController;
import com.ironhack.midtermproject.dao.roles.ThirdParty;
import com.ironhack.midtermproject.repository.ThirdPartyRepository;
import com.ironhack.midtermproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController implements IUserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @PostMapping("/newthirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty newThirdParty(@RequestBody @Valid ThirdParty thirdParty){
        ThirdParty createdParty = new ThirdParty(thirdParty.getName(), thirdParty.getHashKey());
        return thirdPartyRepository.save(createdParty);
    }




}
