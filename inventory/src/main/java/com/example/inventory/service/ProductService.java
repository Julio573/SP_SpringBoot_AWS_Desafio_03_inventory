package com.example.inventory.service;

import com.example.inventory.entities.Product;
import com.example.inventory.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listProducts() {
        log.info("Finding all products");
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        log.info("Finding a product by id {}", id);
        return productRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    public Product registerProduct(Product product) {
        log.info("Registering a new product {}", product);
        return productRepository.save(product);
    }

    @Transactional
    public boolean checkAvailability(Long id, Integer quantity) {
        log.info("Checking if product with id {} is available", id);
        Optional<Product> product = productRepository.findById(Math.toIntExact(id));
        Product addproduct;

        if (product.isPresent()) {
            addproduct = product.get();
            return addproduct.getAmountInStock() >= quantity;
        }
        return false;
    }

    @Transactional
    public Product updateStock(Long id, Integer quantityChange) {
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + id));

        int newAmount = product.getAmountInStock() + quantityChange;

        if (newAmount < 0) {
            log.error("Insufficient stock for product with ID: {}", id);
            throw new IllegalArgumentException("Insufficient stock for product with ID: " + id);
        }

        product.setAmountInStock(newAmount);
        return productRepository.save(product);
    }

    @Transactional
    public void removeProduct(Long id) {
        log.info("Removing product with id {}", id);
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));

        productRepository.delete(product);
    }
}



