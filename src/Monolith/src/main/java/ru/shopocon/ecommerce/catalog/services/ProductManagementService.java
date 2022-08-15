package ru.shopocon.ecommerce.catalog.services;


import org.springframework.data.domain.Pageable;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestFilter;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;

import java.util.List;
import java.util.Optional;

public interface ProductManagementService {

    ProductGetAllResponseModel findAll(Long dealerId,
                                       ProductGetAllRequestFilter filter,
                                       Pageable pageable);

    Optional<Product> findById(Long id);

    void deleteById(Long id);

    void deleteByIdWithDealerIdCheck(Long id, Long dealerId);

    void deleteAllById(List<Long> ids);

    void deleteAllByIdWithDealerIdCheck(List<Long> ids, Long dealerId);
}
