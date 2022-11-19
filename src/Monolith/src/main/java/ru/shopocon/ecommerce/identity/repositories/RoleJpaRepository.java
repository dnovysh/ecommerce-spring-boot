package ru.shopocon.ecommerce.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shopocon.ecommerce.identity.domain.security.Role;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "roles", path = "identity-roles")
public interface RoleJpaRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByOrderByName();
}
