package com.aksulloc.catalog.integrationevents;


import com.aksulloc.catalog.shared.IntegrationEvent;

public interface IntegrationEventService {
    void saveEventAndCatalogContextChanges(IntegrationEvent event);
    void publishThroughEventBus(IntegrationEvent event);
}