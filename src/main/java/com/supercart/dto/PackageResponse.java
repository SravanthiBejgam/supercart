package com.supercart.dto;

import com.supercart.model.Product;
import java.math.BigDecimal;
import java.util.List;

public record PackageResponse (
     String id,
     String name,
     String description,
     List<Product> products,
     BigDecimal price,
     String currency
){}