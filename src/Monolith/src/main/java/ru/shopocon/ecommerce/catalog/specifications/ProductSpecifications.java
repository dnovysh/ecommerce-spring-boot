package ru.shopocon.ecommerce.catalog.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.shopocon.ecommerce.catalog.domain.Category_;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.domain.Product_;

import static ru.shopocon.ecommerce.common.util.StringUtils.isBlank;

public class ProductSpecifications {

    public static Specification<Product> all() {
        return (root, cq, cb) -> cb.conjunction();
    }

    public static Specification<Product> hasDealer(Long dealerId) {
        if (dealerId == null) {
            return null;
        }
        return (root, cq, cb) -> cb.equal(root.get(Product_.dealerId), dealerId);
    }

    public static Specification<Product> skuContainsIgnoreCase(String sku) {
        if (isBlank(sku)) {
            return null;
        }
        return (root, cq, cb) -> cb.like(
            cb.upper(root.get(Product_.sku)),
            cb.upper(cb.literal("%" + sku + "%"))
        );
    }

    public static Specification<Product> hasCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return (root, cq, cb) -> cb.equal(
            root.join(Product_.category).get(Category_.id), categoryId);
    }

    public static Specification<Product> nameContainsIgnoreCase(String name) {
        if (isBlank(name)) {
            return null;
        }
        return (root, cq, cb) -> cb.like(
            cb.upper(root.get(Product_.name)),
            cb.upper(cb.literal("%" + name + "%"))
        );
    }

    public static Specification<Product> isActive(Boolean active) {
        if (active == null) {
            return null;
        }
        if (active) {
            return (root, cq, cb) -> cb.isTrue(root.get(Product_.active));
        }
        return (root, cq, cb) -> cb.isFalse(root.get(Product_.active));
    }

    public static Specification<Product> unitsInStockGreaterThanOrEqualTo(Integer minUnitsInStock) {
        if (minUnitsInStock == null) {
            return null;
        }
        return (root, cq, cb) -> cb.ge(root.get(Product_.unitsInStock), minUnitsInStock);
    }

    public static Specification<Product> unitsInStockLessThanOrEqualTo(Integer maxUnitsInStock) {
        if (maxUnitsInStock == null) {
            return null;
        }
        return (root, cq, cb) -> cb.le(root.get(Product_.unitsInStock), maxUnitsInStock);
    }
}
