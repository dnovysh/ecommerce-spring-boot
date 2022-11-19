package ru.shopocon.ecommerce.catalog.mappers;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;

import java.util.List;

@Component()
public class ProductGetAllResponseModelMapper {
    public ProductGetAllResponseModel mapFrom(Page<Product> productPage) {
        final List<Product> products = productPage.getContent();
        final var page = new PagedModel.PageMetadata(
            productPage.getSize(),
            productPage.getNumber(),
            productPage.getTotalElements(),
            productPage.getTotalPages());
        return new ProductGetAllResponseModel(products, page);
    }
}
