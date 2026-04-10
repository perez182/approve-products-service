package com.nova.approve.products.service.infrastructure.client.providerb;

public record ProductBDTO(
    Long id,
    String title,
    String description,
    String category,
    Double price,
    Double discountPercentage,
    Double rating,
    Integer stock
) {}