package com.curso.account.application;

import com.curso.account.domain.Account;

public interface AccountRepository {

    void emailNotRegistered(String email);
    Account getAccountById(String accountId);
    Account getAccountByEmail(String email);
    Account saveAccount(Account account);

}
