package com.aksulloc.catalog.integrationevents.events;

import com.aksulloc.catalog.shared.IntegrationEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderStatusChangedToAwaitingValidationIntegrationEvent extends IntegrationEvent {
    private final Long orderId;
    private final List<OrderStockItem> orderStockItems;
}

