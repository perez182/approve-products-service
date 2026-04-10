package com.nova.approve.products.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "providers")
@Data
public class ProvidersProperties {
    private String providerA;
    private String providerB;
}