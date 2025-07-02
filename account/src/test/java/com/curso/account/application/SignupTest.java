package com.curso.account.application;

import com.curso.account.application.dto.SignupInput;
import com.curso.account.application.dto.SignupOutput;
import com.curso.account.infra.gateway.MailerGatewayMemory;
import com.curso.account.infra.repository.AccountRepositoryMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupTest {

    AccountRepositoryMemory accountDAO;

    MailerGateway mailerGateway;

    Signup signup;

    @BeforeEach
    public void setUp() {
        accountDAO = new AccountRepositoryMemory();
        mailerGateway = new MailerGatewayMemory();
        signup = new Signup(accountDAO, mailerGateway);
    }

    @Test
    @DisplayName("Deve criar a conta de um passageiro")
    void shouldSignupAccountPassenger(){
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "ABC1234",
                true,
                false,
                "12345678"
        );
        SignupOutput response = signup.execute(signupRequestInput);
        assertNotNull(response.accountId);
    }

    @Test
    void shouldNotSignupDuplicateAccount() {
        var signupRequestInput = new SignupInput(
                "Joao Paulo",
                "joao@gmail.com.br",
                "97456321558",
                "0",
                true,
                false,
                "12345678"
        );
        signup.execute(signupRequestInput);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            signup.execute(signupRequestInput);
        });
        assertEquals("There is already an account with that email", exception.getMessage());
    }

}