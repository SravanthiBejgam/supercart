package com.supercart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreatePackageRequest(
    @NotBlank(message = "Package name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    String name,
    
    @Size(max = 500, message = "Description must be less than 500 characters")
    String description,
    
    @NotEmpty(message = "At least one product ID is required")
    List<@Pattern(regexp = "^[a-zA-Z0-9]{12}$", message = "Invalid product ID format") String> productIds
) {}