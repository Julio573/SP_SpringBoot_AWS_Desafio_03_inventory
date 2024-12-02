package com.example.inventory.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class ProductGlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProductRestError> handleNoSuchElementException(NoSuchElementException ex) {
        log.error("Product not found", ex);
        ProductRestError error = new ProductRestError(ex.getMessage(),  "PRODUCT_NOT_FOUND", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProductRestError> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Invalid Argument provided: {}", ex.getMessage());
        ProductRestError error = new ProductRestError("Invalid Argument", "BAD_REQUEST", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProductRestError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("Method argument type mismatch: {}", ex.getMessage());
        ProductRestError error = new ProductRestError("Please verify the input type.", "BAD_REQUEST", LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProductRestError> handleGeneralException(Exception ex) {
        log.error("Internal server error", ex);
        ProductRestError error = new ProductRestError("No such product",  "400", LocalDateTime.now());
        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }
}
