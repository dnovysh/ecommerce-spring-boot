package ru.shopocon.ecommerce.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shopocon.ecommerce.identity.domain.security.Authority;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "authorities", path = "identity-authorities")
public interface AuthorityJpaRepository extends JpaRepository<Authority, Long> {
    List<Authority> findAllByOrderById();
}
