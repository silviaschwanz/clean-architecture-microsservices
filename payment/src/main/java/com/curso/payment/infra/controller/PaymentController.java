package com.curso.payment.infra.controller;

import com.curso.payment.application.dto.ProcessPaymentInput;
import com.curso.payment.application.dto.ProcessPaymentOutput;
import com.curso.payment.application.usecase.ProcessPayment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final ProcessPayment processPayment;

    public PaymentController(ProcessPayment processPayment) {
        this.processPayment = processPayment;
    }

    @PostMapping("/process_payment")
    public ResponseEntity<ProcessPaymentOutput> processPayment(@RequestBody ProcessPaymentInput input) {
        ProcessPaymentOutput output = processPayment.execute(input);
        return ResponseEntity.ok(output);
    }

}
