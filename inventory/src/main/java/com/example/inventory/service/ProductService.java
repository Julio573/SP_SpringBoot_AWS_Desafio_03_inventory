package com.example.inventory.service;

import com.example.inventory.entities.Product;
import com.example.inventory.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Product updateStock(Long id, Integer quantity) {
        log.info("Updating product with id {} with quantity {}", id, quantity);
        Optional<Product> productOptional = productRepository.findById(Math.toIntExact(id));

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            int newAmount = product.getAmountInStock() - quantity;

            if (newAmount >= 0) {
                product.setAmountInStock(newAmount);
                return productRepository.save(product);
            }
        }
        return null;
    }

    @Transactional
    public void removeProduct(Long id) {
        log.info("Removing product with id {}", id);
        Product product = productRepository.findById(Math.toIntExact(id)).get();
        productRepository.delete(product);
    }

}
