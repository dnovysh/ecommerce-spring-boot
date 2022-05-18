package ru.shopocon.ecommerce.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shopocon.ecommerce.identity.domain.security.User;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
