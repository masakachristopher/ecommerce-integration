package com.ecommerce.order.controller;

import com.ecommerce.order.constant.PathConstants;
import com.ecommerce.order.dto.CreateProductRequest;
import com.ecommerce.order.model.Product;
import com.ecommerce.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(PathConstants.API_V1_PRODUCT)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody CreateProductRequest request) {
        Product created = productService.create(request);
        return ResponseEntity.ok(created);
    }
}