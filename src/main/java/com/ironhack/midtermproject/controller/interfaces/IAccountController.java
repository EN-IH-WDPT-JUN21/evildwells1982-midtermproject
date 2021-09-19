package com.ironhack.midtermproject.controller.interfaces;

import java.util.List;

public interface IAccountController {


    List<Object[]> getAccounts(Long userId);

    List<Object[]> getAccountsById(Long accountId);

}
