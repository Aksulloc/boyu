package com.aksulloc.catalog.integrationevents.events;

import com.aksulloc.catalog.shared.IntegrationEvent;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {
    private final Long orderId;
    private final List<ConfirmedOrderStockItem> orderStockItems;
}