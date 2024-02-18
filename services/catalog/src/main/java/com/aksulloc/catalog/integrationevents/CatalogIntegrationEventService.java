package com.aksulloc.catalog.integrationevents;

import com.aksulloc.catalog.shared.IntegrationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class CatalogIntegrationEventService implements IntegrationEventService {
    private final EntityManager entityManager;
    private final KafkaTemplate<String, IntegrationEvent> kafkaTemplate;
    private final String catalogTopic;

    public CatalogIntegrationEventService(
            EntityManager entityManager,
            KafkaTemplate<String, IntegrationEvent> kafkaTemplate,
            @Value("${spring.kafka.consumer.topic.catalog}") String catalogTopic
    ) {
        this.entityManager = entityManager;
        this.kafkaTemplate = kafkaTemplate;
        this.catalogTopic = catalogTopic;
    }

    @Override
    public void SaveEventAndCatalogContextChanges(IntegrationEvent event) {
        entityManager.getTransaction().commit();
    }

    @Override
    public void PublishThroughEventBus(IntegrationEvent event) {
        kafkaTemplate.send(catalogTopic, event);
    }
}

