package ru.shopocon.ecommerce.catalog.services;

import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shopocon.ecommerce.catalog.domain.Product;
import ru.shopocon.ecommerce.catalog.mappers.ProductGetAllResponseModelMapper;
import ru.shopocon.ecommerce.catalog.model.ProductCreateModel;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestFilter;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;
import ru.shopocon.ecommerce.catalog.model.ProductUpdateModel;
import ru.shopocon.ecommerce.catalog.repositories.CategoryRepository;
import ru.shopocon.ecommerce.catalog.repositories.ProductRepository;
import ru.shopocon.ecommerce.common.exception.exceptions.DealerNotMatchException;

import java.util.List;
import java.util.Optional;

import static ru.shopocon.ecommerce.catalog.specifications.ProductSpecifications.*;

@Service
@Transactional(readOnly = true)
public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    private final ProductGetAllResponseModelMapper responseModelMapper;

    public ProductManagementServiceImpl(ProductRepository productRepository,
                                        CategoryRepository categoryRepository,
                                        ProductGetAllResponseModelMapper responseModelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.responseModelMapper = responseModelMapper;
    }

    @Override
    public ProductGetAllResponseModel findAll(Long dealerId,
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

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional()
    public Product create(ProductCreateModel productCreateModel) {
        val category = categoryRepository
            .findById(productCreateModel.getCategoryId()).orElse(null);
        val product = new Product().toBuilder()
            .dealerId(productCreateModel.getDealerId())
            .sku(productCreateModel.getSku())
            .category(category)
            .name(productCreateModel.getName())
            .description(productCreateModel.getDescription())
            .image(productCreateModel.getImage())
            .active(productCreateModel.isActive())
            .unitsInStock(productCreateModel.getUnitsInStock())
            .unitPrice(productCreateModel.getUnitPrice())
            .build();
        return productRepository.saveAndFlush(product);
    }

    @Override
    @Transactional()
    public Product update(Long id,
                          ProductUpdateModel productUpdateModel,
                          boolean checkDealer,
                          Long dealerId) {
        val optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty()) {
            return null;
        }
        val product = optionalProduct.get();
        if (checkDealer && !product.getDealerId().equals(dealerId)) {
            throw new DealerNotMatchException("Dealer doesn't match");
        }
        product.setSku(productUpdateModel.getSku());
        if (!product.getCategory().getId().equals(productUpdateModel.getCategoryId())) {
            val category = categoryRepository
                .findById(productUpdateModel.getCategoryId()).orElse(null);
            product.setCategory(category);
        }
        product.setName(productUpdateModel.getName());
        product.setDescription(productUpdateModel.getDescription());
        product.setImage(productUpdateModel.getImage());
        product.setActive(productUpdateModel.isActive());
        product.setUnitsInStock(productUpdateModel.getUnitsInStock());
        product.setUnitPrice(productUpdateModel.getUnitPrice());
        productRepository.flush();
        return product;
    }

    @Override
    @Transactional()
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional()
    public void deleteByIdWithDealerIdCheck(Long id, Long dealerId) {
        boolean havingMismatchedDealer = productRepository.existsByIdAndDealerIdNot(id, dealerId);
        if (havingMismatchedDealer) {
            throw new DealerNotMatchException("Dealer doesn't match");
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional()
    public void deleteAllById(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    @Override
    @Transactional()
    public void deleteAllByIdWithDealerIdCheck(List<Long> ids, Long dealerId) {
        boolean havingMismatchedDealers = productRepository.existsByIdInAndDealerIdNot(ids, dealerId);
        if (havingMismatchedDealers) {
            throw new DealerNotMatchException("Dealer doesn't match");
        }
        productRepository.deleteAllById(ids);
    }
}
