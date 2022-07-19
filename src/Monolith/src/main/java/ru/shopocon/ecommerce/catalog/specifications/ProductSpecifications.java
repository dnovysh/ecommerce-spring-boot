package ru.shopocon.ecommerce.catalog.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.shopocon.ecommerce.catalog.domain.Category_;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.domain.Product_;

public class ProductSpecifications {

    public static Specification<Product> all() {
        return (productRoot, cq, cb) -> cb.isTrue(cb.literal(Boolean.TRUE));
    }

    public static Specification<Product> skuContainsIgnoreCase(String sku) {
        return (productRoot, cq, cb) -> cb.like(
            cb.upper(productRoot.get(Product_.sku)),
            "%" + cb.upper(cb.literal(sku)) + "%"
        );
    }

    public static Specification<Product> hasCategory(Long categoryId) {
        return (productRoot, cq, cb) -> cb.equal(
            productRoot.join(Product_.category).get(Category_.id), categoryId);
    }


}
