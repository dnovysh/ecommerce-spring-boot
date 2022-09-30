package ru.shopocon.ecommerce.catalog.model;

import javax.validation.constraints.NotBlank;

public class ProductCreateRequestModel {
    @NotBlank(message = "Product is mandatory")
    ProductCreateModel productCreateModel;
}
