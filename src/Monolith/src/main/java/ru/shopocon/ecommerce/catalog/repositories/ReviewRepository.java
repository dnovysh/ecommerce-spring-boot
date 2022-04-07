package ru.shopocon.ecommerce.catalog.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import ru.shopocon.ecommerce.catalog.domain.Review;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "reviews", path = "catalog-reviews")
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @RestResource(path = "byProductId", rel = "product")
    List<Review> findByProductId(Long productId, Pageable pageable);
}
