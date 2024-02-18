package com.aksulloc.catalog.integrationevents.events;

import com.aksulloc.catalog.shared.IntegrationEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {
    private Long orderId;
    private List<OrderStockItem> orderStockItems;
}