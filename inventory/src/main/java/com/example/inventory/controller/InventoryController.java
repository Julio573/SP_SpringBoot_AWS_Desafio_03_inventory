package com.example.inventory.controller;

import com.example.inventory.entities.Product;
import com.example.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Request to list all product");
        List<Product> products = productService.listProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        log.info("Request to get product {}", id);
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("Request to add a new product {}", product);
        Product newProduct = productService.registerProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping("/availability/{productId}")
    public ResponseEntity<Boolean> getProductAvailability(@PathVariable Long ProductId, @RequestParam Integer amount) {
        log.info("Request to check a product's availability {}", ProductId);
        boolean available = productService.checkAvailability(ProductId, amount);
        return ResponseEntity.status(HttpStatus.OK).body(available);
    }

    @PostMapping("/amount")
    public ResponseEntity<Product> updateProductAmount(@PathVariable Long id, @RequestBody Integer quantity) {
        log.info("Request to update a product's amount {}", id);
        Product updatedProduct = productService.updateStock(id, quantity);
        if (updatedProduct == null) {
            log.error("Could't update product's stoc with ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable String productId) {
        log.info("Request to remove a product from inventory {}", productId);
        productService.removeProduct(Long.valueOf(productId));
        return ResponseEntity.noContent().build();
    }
}
