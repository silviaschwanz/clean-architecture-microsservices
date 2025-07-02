package com.curso.ride.infra.gateway;

import com.curso.ride.application.dto.TransactionInput;
import com.curso.ride.application.dto.TransactionOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientPaymentGateway implements PaymentGateway{

    private final WebClient webClient;

    public WebClientPaymentGateway(
            WebClient.Builder builder,
            @Value("${services.payment.api.url}") String baseUrl
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }


    @Override
    public TransactionOutput processPayment(TransactionInput input) {
        return webClient.post()
                .uri("/process_payment")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(TransactionOutput.class)
                .block(); // Chamada síncrona (como Feign)
    }
}
