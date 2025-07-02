package com.curso.ride.application.usecases;

import com.curso.ride.application.dto.AccountOutput;
import com.curso.ride.application.ports.AccountRepository;
import com.curso.ride.domain.entity.Account;
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
