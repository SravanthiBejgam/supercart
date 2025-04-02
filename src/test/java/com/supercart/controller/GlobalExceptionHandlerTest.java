package com.supercart.controller;
import com.supercart.exception.InvalidProductException;
import com.supercart.exception.PackageNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final WebRequest webRequest = new ServletWebRequest(request);

    @Test
    void handleMethodArgumentNotValid_ShouldReturnValidationError() throws Exception {
        // Create validation error
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError(
            "createPackageRequest", 
            "name", 
            "Name is required"
        );
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        MethodArgumentNotValidException ex = 
            new MethodArgumentNotValidException(null, bindingResult);

        // Execute
        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
            ex, 
            HttpHeaders.EMPTY, 
            HttpStatusCode.valueOf(400),
            webRequest
        );

        // Verify
        GlobalExceptionHandler.ErrorResponse body = 
            (GlobalExceptionHandler.ErrorResponse) response.getBody();
        
        assertEquals(400, body.status());
        assertEquals("Validation Error", body.error());
        assertTrue(body.message().contains("name: Name is required"));
    }

    @Test
    void handlePackageNotFound_ShouldReturn404() {
        PackageNotFoundException ex =
            new PackageNotFoundException("test-id");
        request.setRequestURI("/api/packages/test-id");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = 
            handler.handlePackageNotFound(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Package not found with id: test-id", response.getBody().message());
        assertEquals(404, response.getBody().status());
        assertEquals("/api/packages/test-id", response.getBody().path());
    }

    @Test
    void handleInvalidProduct_ShouldReturn400() {
        InvalidProductException ex =
            new InvalidProductException("Invalid product ID: 123");
        request.setRequestURI("/api/packages");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = 
            handler.handleInvalidProduct(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid product ID: 123", response.getBody().message());
        assertEquals(400, response.getBody().status());
    }

    @Test
    void handleAllExceptions_ShouldReturn500() {
        RuntimeException ex = new RuntimeException("Unexpected error");
        request.setRequestURI("/api/packages");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = 
            handler.handleAllExceptions(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().message());
        assertEquals(500, response.getBody().status());
    }
}