package com.curso.ride.application.ports;

import com.curso.ride.domain.entity.Account;

public interface AccountRepository {

    void emailNotRegistered(String email);
    Account getAccountById(String accountId);
    Account getAccountByEmail(String email);
    Account saveAccount(Account account);

}
