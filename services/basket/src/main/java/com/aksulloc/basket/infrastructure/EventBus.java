package com.aksulloc.basket.infrastructure;

import com.aksulloc.basket.shared.IntegrationEvent;

public interface EventBus {
    void publish(IntegrationEvent event);
}
