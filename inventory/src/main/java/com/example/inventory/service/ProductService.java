package com.example.inventory.service;

import com.example.inventory.entities.Product;
import com.example.inventory.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listProducts() {
        return (List<Product>) productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(Math.toIntExact(id));
    }

    public Product registerProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public boolean checkAvailability(Long id, Integer quantity) {
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
        Product product = productRepository.findById(Math.toIntExact(id)).get();
        productRepository.delete(product);
    }

}
