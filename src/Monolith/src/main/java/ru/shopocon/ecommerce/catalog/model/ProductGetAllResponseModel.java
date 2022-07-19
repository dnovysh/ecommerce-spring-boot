package ru.shopocon.ecommerce.catalog.model;

import org.springframework.hateoas.PagedModel.PageMetadata;
import ru.shopocon.ecommerce.catalog.domain.Product;

import java.util.List;

public record ProductGetAllResponseModel(List<Product> products,
                                         PageMetadata page) {
}
