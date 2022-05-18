package ru.shopocon.ecommerce.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shopocon.ecommerce.identity.domain.security.Authority;

public interface AuthorityJpaRepository extends JpaRepository<Authority, Long> {
}
