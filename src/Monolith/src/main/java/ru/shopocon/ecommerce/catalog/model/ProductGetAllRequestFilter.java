package ru.shopocon.ecommerce.catalog.model;

import lombok.Data;

import static ru.shopocon.ecommerce.common.util.StringUtils.isBlank;

@Data
public class ProductGetAllRequestFilter {
    private String sku;
    private Long categoryId;
    private String name;
    private Boolean active;
    private Integer minUnitsInStock;
    private Integer maxUnitsInStock;

    public boolean isEmpty() {
        return isBlank(sku)
            && categoryId == null
            && isBlank(name)
            && active == null
            && minUnitsInStock == null
            && maxUnitsInStock == null;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }
}
