package controller;

import entities.Product;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ProductService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
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

    @GetMapping("/ping")
    public String ping() {
        return "Hello World";
    }

}
