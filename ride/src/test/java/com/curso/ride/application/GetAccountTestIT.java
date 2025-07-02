package com.curso.ride.application;

import com.curso.ride.ContainersConfig;
import com.curso.ride.application.usecases.GetAccount;
import com.curso.ride.domain.entity.Account;
import com.curso.ride.infra.repository.AccountRepositoryPostgres;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class GetAccountTestIT {

    @Autowired
    AccountRepositoryPostgres accountDAO;

    @Autowired
    private Flyway flyway;

    @Autowired
    GetAccount getAccount;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldGetAccount(){
        var accountSaved = accountDAO.saveAccount(
                Account.create(
                        "Joao Paulo",
                        "joao@gmail.com.br",
                        "97456321558",
                        "ABC1234",
                        true,
                        "12345678"
                )
        );
        var account = getAccount.execute(accountSaved.getAccountId());
        assertEquals(accountSaved.getAccountId(), account.accountId());
        assertEquals("Joao Paulo", account.name());
        assertEquals("joao@gmail.com.br", account.email());
    }

}