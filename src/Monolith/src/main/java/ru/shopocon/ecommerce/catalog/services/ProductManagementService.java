package ru.shopocon.ecommerce.catalog.services;


import org.springframework.data.domain.Pageable;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestFilter;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;

public interface ProductManagementService {

    ProductGetAllResponseModel getAllProducts(Long dealerId,
                                              ProductGetAllRequestFilter filter,
                                              Pageable pageable);
}
