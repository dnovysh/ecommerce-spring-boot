package ru.shopocon.ecommerce.catalog.model;

import lombok.Data;

import java.util.List;

@Data
public class ProductDeleteAllByIdRequestModel {
    private List<Long> ids;
}
