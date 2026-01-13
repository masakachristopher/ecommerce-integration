package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.CreateProductRequest;
import com.ecommerce.order.model.Product;
import com.ecommerce.order.repository.ProductRepository;
import com.ecommerce.order.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Value("${app.configs.currency.base}")
    private String baseCurrency;

    private final ProductRepository productRepository;

    public Product getBySku(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown SKU: " + sku));
    }

    public Product create(CreateProductRequest request){
        if (!baseCurrency.equals(request.baseCurrency())) {
            throw new RuntimeException("Please use configured base currency: " + baseCurrency);
        }

        try {
            Product newProduct = new Product();
            newProduct.setSku(request.sku());
            newProduct.setName(request.name());
            newProduct.setBasePrice(request.basePrice());
            return productRepository.save(newProduct);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save new product into DB");
        }

    }
}

