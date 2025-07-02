package com.curso.account.application;

public interface MailerGateway {
    void send(String recipient, String subject, String message);
}
