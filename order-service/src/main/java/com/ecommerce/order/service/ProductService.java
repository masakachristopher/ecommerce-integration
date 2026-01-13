package com.ecommerce.order.service;

import com.ecommerce.order.dto.CreateProductRequest;
import com.ecommerce.order.model.Product;

public interface ProductService {
    Product getBySku(String sku);
    Product create (CreateProductRequest request);
}

