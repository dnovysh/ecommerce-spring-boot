package ru.shopocon.ecommerce.catalog.controllers;

import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.shopocon.ecommerce.catalog.model.*;
import ru.shopocon.ecommerce.catalog.security.permissions.ProductCreatePermission;
import ru.shopocon.ecommerce.catalog.security.permissions.ProductDeletePermission;
import ru.shopocon.ecommerce.catalog.security.permissions.ProductReadPermission;
import ru.shopocon.ecommerce.catalog.security.permissions.ProductUpdatePermission;
import ru.shopocon.ecommerce.catalog.services.ProductManagementService;
import ru.shopocon.ecommerce.common.exception.exceptions.DealerNotMatchException;
import ru.shopocon.ecommerce.identity.managers.DealerAuthenticationManager;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${spring.data.rest.base-path}/management")
public class ProductManagementController {

    private final ProductManagementService productManagementService;
    private final DealerAuthenticationManager dealerAuthenticationManager;

    public ProductManagementController(ProductManagementService productManagementService,
                                       DealerAuthenticationManager dealerAuthenticationManager) {
        this.productManagementService = productManagementService;
        this.dealerAuthenticationManager = dealerAuthenticationManager;
    }

    @ProductReadPermission
    @GetMapping("/products")
    public ResponseEntity<ProductGetAllResponseModel> getAllProducts(
        @RequestParam(required = false) Long dealerId,
        ProductGetAllRequestFilter filter,
        @PageableDefault() Pageable pageable,
        Authentication authentication
    ) {
        if (hasAuthority(authentication, "product.read")) {
            return ResponseEntity.ok(productManagementService
                .findAll(dealerId, filter, pageable));
        }
        final Long idOfDealerRepresentedByUser = dealerAuthenticationManager.getDealerId(authentication);
        if (idOfDealerRepresentedByUser != null &&
            (dealerId == null || idOfDealerRepresentedByUser.equals(dealerId))) {
            return ResponseEntity.ok(productManagementService
                .findAll(idOfDealerRepresentedByUser, filter, pageable));
        }
        return ResponseEntity.ok(new ProductGetAllResponseModel(
            new ArrayList<>(),
            new PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), 0, 0)
        ));
    }

    @ProductReadPermission
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseModel> getProductById(
        @PathVariable Long id, Authentication authentication
    ) {
        val optionalProduct = productManagementService.findById(id);
        if (optionalProduct.isPresent()) {
            val product = optionalProduct.get();
            if (hasAuthority(authentication, "product.read")) {
                return ResponseEntity.ok(new ProductResponseModel(product));
            }
            final Long idOfDealerRepresentedByUser = dealerAuthenticationManager.getDealerId(authentication);
            if (idOfDealerRepresentedByUser != null &&
                idOfDealerRepresentedByUser.equals(product.getDealerId())) {
                return ResponseEntity.ok(new ProductResponseModel(product));
            }
        }
        throw new ResponseStatusException(NOT_FOUND, "Product Not Found");
    }

    @ProductCreatePermission
    @PostMapping("/products")
    public ResponseEntity<ProductResponseModel> createProduct(
        @Valid @RequestBody ProductCreateRequestModel productCreateRequestModel,
        Authentication authentication
    ) {
        val productCreateModel = productCreateRequestModel.getProductCreateModel();
        if (hasAuthority(authentication, "product.create")) {
            if (productCreateModel.getDealerId() == null) {
                throw new ResponseStatusException(BAD_REQUEST, "Dealer must be specified");
            }
        } else {
            final Long idOfDealerRepresentedByUser = dealerAuthenticationManager.getDealerId(authentication);
            if (productCreateModel.getDealerId() != null &&
                !productCreateModel.getDealerId().equals(idOfDealerRepresentedByUser)) {
                throw new AccessDeniedException("You are not a representative of the specified dealer");
            }
            productCreateModel.setDealerId(idOfDealerRepresentedByUser);
        }
        val product = productManagementService.create(productCreateModel);
        return ResponseEntity.ok(new ProductResponseModel(product));
    }

    @ProductUpdatePermission
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseModel> updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductUpdateRequestModel productUpdateRequestModel,
        Authentication authentication
    ) {
        val productUpdateModel = productUpdateRequestModel.getProductUpdateModel();
        if (hasAuthority(authentication, "product.update")) {
            return ResponseEntity.ok(new ProductResponseModel(
                productManagementService.update(id, productUpdateModel, false, null)
            ));
        } else {
            final Long idOfDealerRepresentedByUser = dealerAuthenticationManager.getDealerId(authentication);
            return ResponseEntity.ok(new ProductResponseModel(
                productManagementService.update(id, productUpdateModel, true, idOfDealerRepresentedByUser)
            ));
        }
    }

    @ProductDeletePermission
    @DeleteMapping("/products")
    public void deleteAllProductsById(@Valid @RequestBody ProductDeleteAllByIdRequestModel requestModel,
                                      Authentication authentication) {
        final List<Long> ids = requestModel.getIds();
        if (ids == null || ids.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Identifiers must be specified");
        }
        if (hasAuthority(authentication, "product.delete")) {
            productManagementService.deleteAllById(ids);
            return;
        }
        final Long idOfDealerRepresentedByUser = dealerAuthenticationManager.getDealerId(authentication);
        try {
            productManagementService.deleteAllByIdWithDealerIdCheck(ids, idOfDealerRepresentedByUser);
        } catch (DealerNotMatchException ex) {
            throw new AccessDeniedException(ex.getMessage(), ex);
        }
    }

    @ProductDeletePermission
    @DeleteMapping("/products/{id}")
    public void deleteProductById(@PathVariable Long id, Authentication authentication) {
        if (hasAuthority(authentication, "product.delete")) {
            productManagementService.deleteById(id);
            return;
        }
        final Long idOfDealerRepresentedByUser = dealerAuthenticationManager.getDealerId(authentication);
        try {
            productManagementService.deleteByIdWithDealerIdCheck(id, idOfDealerRepresentedByUser);
        } catch (DealerNotMatchException ex) {
            throw new AccessDeniedException(ex.getMessage(), ex);
        }
    }

    private boolean hasAuthority(Authentication authentication, String authority) {
        return authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }
}
