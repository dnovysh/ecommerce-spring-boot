package ru.shopocon.ecommerce.catalog.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.mappers.ProductGetAllResponseModelMapper;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestFilter;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;
import ru.shopocon.ecommerce.catalog.repositories.ProductRepository;

import static ru.shopocon.ecommerce.catalog.specifications.ProductSpecifications.*;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductRepository productRepository;
    private final ProductGetAllResponseModelMapper responseModelMapper;

    public ProductManagementServiceImpl(ProductRepository productRepository,
                                        ProductGetAllResponseModelMapper responseModelMapper) {
        this.productRepository = productRepository;
        this.responseModelMapper = responseModelMapper;
    }

    @Override
    public ProductGetAllResponseModel getAllProducts(Long dealerId,
                                                     ProductGetAllRequestFilter filter,
                                                     Pageable pageable) {
        final boolean requestContainsDealer = dealerId != null;
        final boolean requestContainsFilter = filter != null && filter.isNotEmpty();

        if (!requestContainsFilter) {
            if (!requestContainsDealer) {
                return responseModelMapper.mapFrom(productRepository.findAll(pageable));
            }
            return responseModelMapper.mapFrom(productRepository.findAllByDealerId(dealerId, pageable));
        }

        Specification<Product> spec = all()
            .and(hasDealer(dealerId))
            .and(skuContainsIgnoreCase(filter.getSku()))
            .and(hasCategory(filter.getCategoryId()))
            .and(nameContainsIgnoreCase(filter.getName()))
            .and(isActive(filter.getActive()))
            .and(unitsInStockGreaterThanOrEqualTo(filter.getMinUnitsInStock()))
            .and(unitsInStockLessThanOrEqualTo(filter.getMaxUnitsInStock()));

        return responseModelMapper.mapFrom(productRepository.findAll(spec, pageable));
    }
}
