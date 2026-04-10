package com.nova.approve.products.service.infrastructure.client.providera;


public record ProductADTO(
    Long id,
    String title,
    Double price,
    String description,
    String category,
    String image,
    RatingDTO rating
) {}
