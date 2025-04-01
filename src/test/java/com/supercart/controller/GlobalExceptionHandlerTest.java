package com.supercart.controller;

import com.supercart.exception.InvalidProductException;
import com.supercart.exception.PackageNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlePackageNotFound_ShouldReturn404() {
        // Arrange
        PackageNotFoundException ex = new PackageNotFoundException("1");

        // Act
        ResponseEntity<String> response = handler.handlePackageNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify full message match
        assertEquals(
                "Package not found with id: 1", // Exact match
                response.getBody()
        );

        // OR verify substring exists
        assertTrue(response.getBody().contains("Package not found with id"));
    }

    @Test
    void handleInvalidProduct_ShouldReturn400() {
        ResponseEntity<String> response = handler.handleInvalidProduct(
                new InvalidProductException("Invalid product"));
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid product"));
    }
}