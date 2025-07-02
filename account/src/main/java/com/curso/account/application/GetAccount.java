package com.curso.account.application;

import com.curso.account.application.dto.AccountOutput;
import com.curso.account.domain.Account;
import org.springframework.stereotype.Service;

@Service
public class GetAccount {

    private final AccountRepository accountRepository;

    public GetAccount(AccountRepository accountDAO) {
        this.accountRepository = accountDAO;
    }

    public AccountOutput execute(String accountId) {
        Account account = accountRepository.getAccountById(accountId);
        return new AccountOutput(
                account.getAccountId(),
                account.getName(),
                account.getEmail(),
                account.getCpf(),
                account.getCarPlate(),
                account.isPassenger(),
                account.isDriver()
        );
    }

}
