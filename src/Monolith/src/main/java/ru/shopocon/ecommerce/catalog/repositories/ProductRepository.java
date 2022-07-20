package ru.shopocon.ecommerce.catalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shopocon.ecommerce.catalog.domain.Product;

@RepositoryRestResource(collectionResourceRel = "products", path = "catalog-products")
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @RestResource(path = "queryAllDefault", rel = "defaultQueryAllMethod")
    Page<Product> queryAllByOrderByPopularityIndexDescRatingDescIdDesc(Pageable pageable);

    @RestResource(path = "findByCategoryId", rel = "findByCategory")
    Page<Product> findByCategoryIdOrderByPopularityIndexDescRatingDescIdDesc(
        @RequestParam Long id, Pageable pageable
    );

    @RestResource(path = "findByProductName", rel = "findByProduct")
    Page<Product> findByNameContainingIgnoreCaseOrderByPopularityIndexDescRatingDescIdDesc(
        @RequestParam String name, Pageable pageable
    );

    @RestResource(path = "findByCategoryIdAndProductName", rel = "findByCategoryAndProduct")
    Page<Product> findByCategoryIdAndNameContainingIgnoreCaseOrderByPopularityIndexDescRatingDescIdDesc(
        @RequestParam Long id, @RequestParam(required = false) String name, Pageable pageable
    );

    Page<Product> findAllByDealerId(long dealerId, Pageable pageable);
}
