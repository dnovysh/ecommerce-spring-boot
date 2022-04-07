package ru.shopocon.ecommerce.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shopocon.ecommerce.catalog.domain.Category;

@RepositoryRestResource(collectionResourceRel = "categories", path = "catalog-categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
