package com.ironhack.midtermproject.controller.impl;

import com.ironhack.midtermproject.controller.interfaces.IUserController;
import com.ironhack.midtermproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements IUserController {

    @Autowired
    UserRepository userRepository;


}
