package ru.shopocon.ecommerce.catalog.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestModel;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;
import ru.shopocon.ecommerce.catalog.repositories.ProductRepository;

import java.util.List;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductRepository productRepository;

    public ProductManagementServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductGetAllResponseModel getAllProducts(Long dealerId,
                                                     ProductGetAllRequestModel requestDto,
                                                     Pageable pageable) {
        Page<Product> findAllResult = productRepository.findAllByDealerId(dealerId, pageable);
        List<Product> products = findAllResult.getContent();
        final var page = new PagedModel.PageMetadata(
            findAllResult.getSize(),
            findAllResult.getNumber(),
            findAllResult.getTotalElements(),
            findAllResult.getTotalPages());
        return new ProductGetAllResponseModel(products, page);
    }
}
