package ru.shopocon.ecommerce.catalog.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllRequestModel;
import ru.shopocon.ecommerce.catalog.model.ProductGetAllResponseModel;
import ru.shopocon.ecommerce.catalog.services.ProductManagementService;

@RestController
@RequestMapping("${spring.data.rest.base-path}/management")
public class ProductManagementController {

    private final ProductManagementService productManagementService;

    public ProductManagementController(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    @GetMapping("/products")
    public ResponseEntity<ProductGetAllResponseModel> getAllProducts(
        @RequestParam(required = false) Long dealerId,
        ProductGetAllRequestModel requestDto,
        @PageableDefault(sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(productManagementService
            .getAllProducts(dealerId, requestDto, pageable));
    }
}
