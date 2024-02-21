package com.aksulloc.basket.integrationevents.eventhanding;

import com.aksulloc.basket.integrationevents.events.OrderStartedIntegrationEvent;
import com.aksulloc.basket.model.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderStartedIntegrationEventHandler {
    private final BasketRepository basketRepository;

    @KafkaListener(topics = "${spring.kafka.consumer.topic.order}")
    public void handle(OrderStartedIntegrationEvent event) {
        System.out.printf("----- Handling integration event: %s (%s)", event.getId(), event.getClass().getSimpleName());
        basketRepository.deleteBasket(event.getUserId());
    }
}
