package com.curso.ride.data;

import com.curso.ride.domain.entity.Account;

public class AccountFactory {

    public static Account createAccountPassanger() {
        return Account.create(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "ABC1234",
                false,
                "12345678"
        );
    }

    public static Account createAccountDriver() {
        return Account.create(
                "Luiz Vinicius",
                "luv@gmail.com.br",
                "12345678909",
                "ABC1234",
                true,
                "12345691"
        );
    }
}
