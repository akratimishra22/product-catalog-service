package com.gharelu.product_catalog_service.service;

import com.gharelu.product_catalog_service.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductById(Long id);
    void deleteProduct(Long id);
    Product updateProduct(Long id, Product updatedProduct);
}

