package ru.shopocon.ecommerce.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shopocon.ecommerce.catalog.domain.Category;
import ru.shopocon.ecommerce.catalog.security.permissions.CategoryReadPermission;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "categories", path = "catalog-categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByName();

    @CategoryReadPermission()
    List<Category> findAllPreAuthorizedByOrderByName();
}
