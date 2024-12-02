package com.example.inventory.controller;

import com.example.inventory.entities.Product;
import com.example.inventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final ProductService productService;


    @Operation(summary = "Returns all products", description = "Returns all products on the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Products found successfully",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product database Empty",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Request to list all product");
        List<Product> products = productService.listProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Returns an specific product", description = "Finds a product by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found successfully",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        log.info("Request to get product {}", id);
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Adds a new Product", description = "Resource to add a new product",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product added successfully",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "422", description = "Invalid input data",
                            content = @Content(mediaType = "application/json"))})
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        log.info("Request to add a new product {}", product);
        Product newProduct = productService.registerProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @Operation(summary = "Checks availability", description = "Checks availability of a product by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product found successfully",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json"))})
    @GetMapping("/availability/{id}")
    public ResponseEntity<Map<String, Object>> getProductAvailability(@PathVariable Long id) {
        log.info("Request to check a product's availability {}", id);
        Product product = productService.findById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("productId", product.getId());
        response.put("productName", product.getName());
        response.put("availableStock", product.getAmountInStock());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Updates Quantity", description = "Increases or decreases a product's quantidade",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Quantity changed successfully",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json"))})
    @PostMapping("/amount/{id}")
    public ResponseEntity<Product> updateProductAmount(@PathVariable Long id, @RequestBody Integer quantity) {
        log.info("Request to update a product's amount {}", id);
        Product product = productService.findById(id);
        if (product == null) {
            log.error("Product not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Current stock of product ID {}: {}", id, product.getAmountInStock());
        Product updatedProduct = productService.updateStock(id, quantity);
        if (updatedProduct == null) {
            log.error("Couldn't update product's stock with ID: {}", id);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Deletes a product", description = "Deletes a product from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product deleted",
                            content = @Content(mediaType = "application/JSON", schema = @Schema(implementation = Product.class))),
                    @ApiResponse(responseCode = "404", description = "Product not found",
                            content = @Content(mediaType = "application/json"))})
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable String id) {
        log.info("Request to remove a product from inventory {}", id);
        productService.removeProduct(Long.valueOf(id));
        return ResponseEntity.noContent().build();
    }
}
