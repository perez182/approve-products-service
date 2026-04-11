package com.nova.approve.products.service.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.nova.approve.products.service.config.ProvidersProperties;
import com.nova.approve.products.service.infrastructure.client.http.ExternalApiClient;
import com.nova.approve.products.service.infrastructure.client.providera.ProductADTO;
import com.nova.approve.products.service.infrastructure.client.providerb.ProductBDTO;
import com.nova.approve.products.service.infrastructure.client.providerb.ProductsBDTO;

@Service
@AllArgsConstructor
@Slf4j
public class InventoryService {

    private final ExternalApiClient externalApiClient;
    private final ProvidersProperties providers;




    public List<ProductADTO> getProductsFromProviderA() {
        try {
            ProductADTO[] response =
            externalApiClient.get(providers.getProviderA(), ProductADTO[].class);
        return Arrays.asList(response);
        } catch (Exception e) {
            log.error("Provider A failed", e.getMessage());
            return List.of(); 
        }

    }

    public List<ProductBDTO> getProductsFromProviderB() {
        try{
            ProductsBDTO response =
                    externalApiClient.get(providers.getProviderB(), ProductsBDTO.class);
            return response.products();
        } catch (Exception e) {
            log.error("Provider B failed", e.getMessage());
            return List.of(); 
        }
    }
}