package com.example.productservice.controller;

import com.example.productservice.entity.Product;
import com.example.productservice.payload.ApiResponse;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Product product) {
        productService.save(product);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        Optional<Product> product = productService.getById(id);
        if(product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Product not found"));
        }
    }
}
