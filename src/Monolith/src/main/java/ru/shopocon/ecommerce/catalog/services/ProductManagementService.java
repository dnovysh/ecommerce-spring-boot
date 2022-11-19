package ru.shopocon.ecommerce.catalog.services;


import org.springframework.data.domain.Pageable;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.model.ProductCreateModel;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestFilter;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;
import ru.shopocon.ecommerce.catalog.model.ProductUpdateModel;

import java.util.List;
import java.util.Optional;

public interface ProductManagementService {

    ProductGetAllResponseModel findAll(Long dealerId,
                                       ProductGetAllRequestFilter filter,
                                       Pageable pageable);

    Optional<Product> findById(Long id);

    Product create(ProductCreateModel productCreateModel);

    Product update(Long id, ProductUpdateModel productUpdateModel, boolean checkDealer, Long dealerId);

    void deleteById(Long id);

    void deleteByIdWithDealerIdCheck(Long id, Long dealerId);

    void deleteAllById(List<Long> ids);

    void deleteAllByIdWithDealerIdCheck(List<Long> ids, Long dealerId);
}
