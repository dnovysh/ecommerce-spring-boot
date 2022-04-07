package ru.shopocon.ecommerce.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shopocon.ecommerce.catalog.domain.Product;

@RepositoryRestResource(collectionResourceRel = "products", path = "catalog-products")
public interface ProductRepository extends JpaRepository<Product, Long> {
}
