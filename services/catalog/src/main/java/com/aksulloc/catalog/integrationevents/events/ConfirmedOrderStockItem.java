package com.aksulloc.catalog.integrationevents.events;

public record ConfirmedOrderStockItem(Long productId, Boolean hasStock) {
}