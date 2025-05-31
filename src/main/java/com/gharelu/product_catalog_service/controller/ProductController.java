package com.gharelu.product_catalog_service.controller;

import com.gharelu.product_catalog_service.model.Product;
import com.gharelu.product_catalog_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @Autowired
    ApplicationContext ctx;

    private String isTokenValid(String authHeader) {
        WebClient webClient = ctx.getBean("authServiceWebClientEurekaDiscovered", WebClient.class);
        String authResponse = webClient.get()
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return authResponse;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                 @RequestBody Product product) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return ResponseEntity.ok(service.createProduct(product));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return ResponseEntity.ok(service.getAllProducts());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                              @PathVariable Long id) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return service.getProductById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                                 @PathVariable Long id, @RequestBody Product updated) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid"))
            return ResponseEntity.ok(service.updateProduct(id, updated));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                                              @PathVariable Long id) {
        String authResponse = isTokenValid(authHeader);
        if(authResponse.equals("Valid")){
            service.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
