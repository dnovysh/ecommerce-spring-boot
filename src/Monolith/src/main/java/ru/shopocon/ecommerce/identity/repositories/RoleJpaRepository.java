package ru.shopocon.ecommerce.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shopocon.ecommerce.identity.domain.security.Role;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {
}
