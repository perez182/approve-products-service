package com.nova.approve.products.service.infrastructure.client.http;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ExternalApiClient {

    private final WebClient webClient;

    public ExternalApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> T get(String url, Class<T> responseType) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("Error calling API: " + body))
                )
                .bodyToMono(responseType)
                .block(); 
    }
}