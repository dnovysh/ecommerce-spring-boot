package ru.shopocon.ecommerce.catalog.services;


import org.springframework.data.domain.Pageable;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestModel;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;

public interface ProductManagementService {

    ProductGetAllResponseModel getAllProducts(Long dealerId,
                                              ProductGetAllRequestModel requestDto,
                                              Pageable pageable);
}
