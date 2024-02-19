package com.aksulloc.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class CatalogBrand extends AbstractEntity {
    @Column(nullable = false, length = 100)
    private String brand;
}