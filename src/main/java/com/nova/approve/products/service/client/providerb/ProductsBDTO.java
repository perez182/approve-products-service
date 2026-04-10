package com.nova.approve.products.service.client.providerb;

import java.util.List;

public record ProductsBDTO(
    List<ProductBDTO> products
) {}