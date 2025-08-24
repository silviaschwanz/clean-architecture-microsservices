package com.curso.payment;

import org.springframework.boot.SpringApplication;

public class TestPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.from(PaymentApplication::main).with(ContainersConfig.class).run(args);
	}

}
