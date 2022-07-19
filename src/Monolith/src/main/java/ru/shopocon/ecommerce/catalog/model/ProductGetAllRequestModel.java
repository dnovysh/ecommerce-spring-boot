package ru.shopocon.ecommerce.catalog.model;

import lombok.Data;

@Data
public class ProductGetAllRequestModel {
    private String sku;
    private String categoryId;
    private String name;
    private Boolean active;
    private Integer minUnitsInStock;
    private Integer maxUnitsInStock;
}
