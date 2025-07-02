package com.curso.ride.application.ports;

public interface MailerGateway {
    void send(String recipient, String subject, String message);
}
