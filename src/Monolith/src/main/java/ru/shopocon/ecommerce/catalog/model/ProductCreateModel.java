package ru.shopocon.ecommerce.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductCreateModel {
    private Long dealerId;

    @NotBlank(message = "Sku is mandatory")
    private String sku;

    @NotNull(message = "Category is mandatory")
    private Long categoryId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String description;

    private String image;

    @NotNull(message = "Active is mandatory")
    private boolean active;

    private int unitsInStock;

    @NotNull(message = "Price is mandatory")
    private BigDecimal unitPrice;
}
