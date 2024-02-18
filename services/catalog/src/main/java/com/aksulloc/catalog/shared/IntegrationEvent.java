package com.aksulloc.catalog.shared;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class IntegrationEvent {
    private UUID id;
    private LocalDateTime creationDate;

    public IntegrationEvent() {
        this(UUID.randomUUID(), LocalDateTime.now());
    }
}
