package com.aksulloc.catalog.integrationevents.events;

import com.aksulloc.catalog.shared.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderStockConfirmedIntegrationEvent extends IntegrationEvent {
    private final Long orderId;
}
