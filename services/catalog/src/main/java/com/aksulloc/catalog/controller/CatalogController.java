package com.aksulloc.catalog.controller;

import com.aksulloc.catalog.integrationevents.IntegrationEventService;
import com.aksulloc.catalog.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.aksulloc.catalog.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RequestMapping("catalog")
@RestController
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogItemRepository catalogItemRepository;
    private final CatalogTypeRepository catalogTypeRepository;
    private final CatalogBrandRepository catalogBrandRepository;
    private final IntegrationEventService integrationEventService;
    private final EntityManager entityManager;

    @RequestMapping("items")
    public Page<CatalogItem> catalogItems(
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "0", required = false) Integer pageIndex
    ) {
        return catalogItemRepository.findAll(PageRequest.of(pageIndex, pageSize));
    }

    @RequestMapping("items/{id}")
    public ResponseEntity<CatalogItem> catalogItems(@PathVariable Long id) {
        return ResponseEntity.of(catalogItemRepository.findById(id));
    }

    @RequestMapping("items/withname/{name}")
    public Page<CatalogItem> catalogItems(
            @RequestParam(defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(defaultValue = "0", required = false) Integer pageIndex,
            @PathVariable String name // TODO length should be min 1 character
    ) {
        return null; //return catalogItemRepository.findAllByName(name);
    }

    @RequestMapping("catalogtypes")
    public Iterable<CatalogType> findAllCatalogTypes() {
        return catalogTypeRepository.findAll();
    }

    @RequestMapping("catalogbrands")
    public Iterable<CatalogBrand> findAllCatalogBrands() {
        return catalogBrandRepository.findAll();
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT, path = "items")
    public void updateProduct(@RequestBody CatalogItem productToUpdate) {
        catalogItemRepository.findById(productToUpdate.getId())
                .ifPresentOrElse(catalogItem -> {
                    var oldPrice = catalogItem.getPrice();
                    var raiseProductPriceChangedEvent = !oldPrice.equals(productToUpdate.getPrice());

                    entityManager.getTransaction().begin();
                    catalogItem = productToUpdate;
                    entityManager.persist(catalogItem);

                    if (raiseProductPriceChangedEvent) {
                        //Create Integration Event to be published through the Event Bus
                        var priceChangedEvent = new ProductPriceChangedIntegrationEvent(
                                catalogItem.getId(),
                                productToUpdate.getPrice(),
                                oldPrice
                        );

                        // Achieving atomicity between original Catalog database operation and the IntegrationEventLog thanks to a local transaction
                        integrationEventService.saveEventAndCatalogContextChanges(priceChangedEvent);

                        // Publish through the Event Bus and mark the saved event as published
                        integrationEventService.publishThroughEventBus(priceChangedEvent);
                    } else {
                        entityManager.getTransaction().commit();
                    }
                }, () -> {
                    throw new NotFoundException(String.format("Item with id %d not found.", productToUpdate.getId()));
                });

    }

    @RequestMapping(method = RequestMethod.POST, path = "items")
    public void createProduct(@RequestBody CatalogItem product) {

    }

    @RequestMapping(method = RequestMethod.DELETE, path = "items/{id}")
    public void deleteProduct(@PathVariable Long id) {

    }
}

