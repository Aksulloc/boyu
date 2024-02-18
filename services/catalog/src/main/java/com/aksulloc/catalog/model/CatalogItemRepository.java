package com.aksulloc.catalog.model;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CatalogItemRepository extends PagingAndSortingRepository<CatalogItem, Long> {
//    Page<CatalogItem> findAllByName(String name);
}