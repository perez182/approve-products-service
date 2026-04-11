package com.nova.approve.products.service.api.dto;

public record ProductResponseDTO(
        Long id,
        String externalId,
        String name,
        Double price,
        Double rating,
        Integer stock,
        String provider,
        String category,
        String brand,
        String image
) {}