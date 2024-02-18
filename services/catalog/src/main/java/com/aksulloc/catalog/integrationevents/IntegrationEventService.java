package com.aksulloc.catalog.integrationevents;


import com.aksulloc.catalog.shared.IntegrationEvent;

public interface IntegrationEventService {
    void SaveEventAndCatalogContextChanges(IntegrationEvent event);
    void PublishThroughEventBus(IntegrationEvent event);
}