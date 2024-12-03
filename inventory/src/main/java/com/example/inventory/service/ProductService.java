package com.example.inventory.service;

import com.example.inventory.entities.Product;
import com.example.inventory.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

//    @Transactional
//    public boolean checkAvailability(Long id, Integer quantity) {
//        log.info("Verifies a product availability {}", id);
//        Product product = productRepository.findById(Math.toIntExact(id))
//                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + id));
//        boolean isAvailable = product.getAmountInStock() >= quantity;
//        if (!isAvailable) {
//            log.warn("Insufficient stock for product with ID: {}. Amount required: {}, Available: {}", id, quantity, product.getAmountInStock());
//            throw new IllegalArgumentException("Insufficient stock for product with ID: " + id);
//        }
//        return true;
//    }

    @Transactional
    public Product updateStock(Long id, Integer quantity) {
        log.info("Updating stock for product with ID: {}. Adjusting by: {}", id, quantity);
        Product product = productRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + id));

        int newAmount = product.getAmountInStock() + quantity;
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



