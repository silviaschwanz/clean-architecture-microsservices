package com.curso.payment;

import com.curso.payment.infra.fallback.PaymentMethod;
import com.curso.payment.infra.fallback.PaymentProcessorFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
        PaymentProcessorFactory.createChain(PaymentMethod.CIELO);
    }

}
