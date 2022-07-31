package ru.shopocon.ecommerce.identity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.shopocon.ecommerce.identity.domain.Dealer;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "dealers", path = "identity-dealers")
public interface DealerJpaRepository extends JpaRepository<Dealer, Long> {

    List<Dealer> findAllByOrderByName();
}
