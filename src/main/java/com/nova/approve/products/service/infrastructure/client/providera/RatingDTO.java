package com.nova.approve.products.service.infrastructure.client.providera;

public record RatingDTO(
    Double rate,
    Integer count
) {}