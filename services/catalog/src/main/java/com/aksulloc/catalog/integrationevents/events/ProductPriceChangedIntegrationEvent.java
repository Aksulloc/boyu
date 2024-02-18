package com.aksulloc.catalog.integrationevents.events;

import com.aksulloc.catalog.shared.IntegrationEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public class ProductPriceChangedIntegrationEvent extends IntegrationEvent {
    private final Long productId;
    private final BigDecimal newPrice;
    private final BigDecimal oldPrice;
}
