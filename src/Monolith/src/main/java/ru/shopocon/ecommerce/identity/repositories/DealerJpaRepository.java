package ru.shopocon.ecommerce.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shopocon.ecommerce.identity.domain.Dealer;

public interface DealerJpaRepository extends JpaRepository<Dealer, Long> {
}
