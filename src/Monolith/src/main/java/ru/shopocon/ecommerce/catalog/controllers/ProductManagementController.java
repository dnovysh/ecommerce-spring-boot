package ru.shopocon.ecommerce.catalog.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestFilter;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;
import ru.shopocon.ecommerce.catalog.services.ProductManagementService;
import ru.shopocon.ecommerce.identity.managers.DealerAuthenticationManager;

import java.util.ArrayList;

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

    @PreAuthorize("hasAuthority('product.read') OR" +
        " (hasAuthority('dealer.product.read')" +
        " AND @dealerAuthenticationManager.isDealerRepresentative(authentication))")
    @GetMapping("/products")
    public ResponseEntity<ProductGetAllResponseModel> getAllProducts(
        @RequestParam(required = false) Long dealerId,
        ProductGetAllRequestFilter filter,
        @PageableDefault() Pageable pageable,
        Authentication authentication
    ) {
        final boolean hasProductReadAuthority = authentication.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("product.read"));
        if (hasProductReadAuthority) {
            return ResponseEntity.ok(productManagementService
                .getAllProducts(dealerId, filter, pageable));
        }
        final Long idOfDealerRepresentedByUser = dealerAuthenticationManager.getDealerId(authentication);
        if (idOfDealerRepresentedByUser != null &&
            (dealerId == null || idOfDealerRepresentedByUser.equals(dealerId))) {
            return ResponseEntity.ok(productManagementService
                .getAllProducts(idOfDealerRepresentedByUser, filter, pageable));
        }
        return ResponseEntity.ok(new ProductGetAllResponseModel(
            new ArrayList<>(),
            new PagedModel.PageMetadata(
                pageable.getPageSize(), pageable.getPageNumber(), 0, 0)
        ));
    }
}
