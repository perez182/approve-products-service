package com.nova.approve.products.service.api.dto;

public record ProductResponseDTO(
        Long id,
        String name,
        Double price,
        Double rating,
        Integer stock,
        String provider,
        String category,
        String brand
) {}