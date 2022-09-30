package ru.shopocon.ecommerce.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateModel {
    private Long dealerId;

    @NotBlank(message = "Sku is mandatory")
    private String sku;

    @NotBlank(message = "Category is mandatory")
    private Long categoryId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    private String image;

    @NotBlank(message = "Active is mandatory")
    private boolean active;

    @NotBlank(message = "UnitsInStock is mandatory")
    private int unitsInStock;

    @NotBlank(message = "Price is mandatory")
    private BigDecimal unitPrice;
}
