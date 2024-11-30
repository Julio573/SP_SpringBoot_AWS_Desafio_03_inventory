package com.example.inventory.controller;

import com.example.inventory.entities.Product;
import com.example.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.listProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product newProduct = productService.registerProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping("/availability/{productId}")
    public ResponseEntity<Boolean> getProductAvailability(@PathVariable long productId, @RequestParam Integer amount) {
        boolean available = productService.checkAvailability(productId, amount);
        return ResponseEntity.status(HttpStatus.OK).body(available);
    }

    @PostMapping("/amount")
    public ResponseEntity<Product> updateProductAmount(@PathVariable Long id, @RequestBody Integer quantity) {
        Product product = productService.updateStock(id, quantity);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long productId) {
        productService.removeProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
