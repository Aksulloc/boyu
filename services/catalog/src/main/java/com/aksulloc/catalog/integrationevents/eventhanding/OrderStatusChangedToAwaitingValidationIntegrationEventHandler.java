package com.aksulloc.catalog.integrationevents.eventhanding;

import com.aksulloc.catalog.integrationevents.IntegrationEventService;
import com.aksulloc.catalog.integrationevents.events.ConfirmedOrderStockItem;
import com.aksulloc.catalog.integrationevents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import com.aksulloc.catalog.integrationevents.events.OrderStockConfirmedIntegrationEvent;
import com.aksulloc.catalog.integrationevents.events.OrderStockRejectedIntegrationEvent;
import com.aksulloc.catalog.model.CatalogItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@AllArgsConstructor
@Component
public class OrderStatusChangedToAwaitingValidationIntegrationEventHandler {
    private final CatalogItemRepository catalogItemRepository;
    private final IntegrationEventService integrationEventService;

    @KafkaListener(topics = "${spring.kafka.consumer.topic.order}")
    public void handle(OrderStatusChangedToAwaitingValidationIntegrationEvent event, Acknowledgment acknowledgment) {
        var confirmedOrderStockItems = new ArrayList<ConfirmedOrderStockItem>();

        for (var orderStockItem : event.getOrderStockItems()) {
            catalogItemRepository.findById(orderStockItem.productId()).ifPresent(catalogItem -> {
                var hasStock = catalogItem.getAvailableStock() >= orderStockItem.units();
                var confirmedStockItem = new ConfirmedOrderStockItem(catalogItem.getId(), hasStock);
                confirmedOrderStockItems.add(confirmedStockItem);
            });
        }

        var confirmedIntegrationEvent = confirmedOrderStockItems.stream()
                .anyMatch(c -> !c.hasStock())
                ? new OrderStockRejectedIntegrationEvent(event.getOrderId(), confirmedOrderStockItems)
                : new OrderStockConfirmedIntegrationEvent(event.getOrderId());

        integrationEventService.saveEventAndCatalogContextChanges(confirmedIntegrationEvent);
        integrationEventService.publishThroughEventBus(confirmedIntegrationEvent);
        acknowledgment.acknowledge();
    }
}