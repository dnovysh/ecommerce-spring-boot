package ru.shopocon.ecommerce.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateRequestModel {
    @NotNull(message = "Product is mandatory")
    ProductCreateModel productCreateModel;
}
