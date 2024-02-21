package com.aksulloc.basket.integrationevents.events;

import com.aksulloc.basket.shared.IntegrationEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderStartedIntegrationEvent extends IntegrationEvent {
    private final String userId;
}
