package com.supercart.dto;

import com.supercart.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class PackageResponse {
    private String id;
    private String name;
    private String description;
    private List<Product> products;
    private BigDecimal price;
    private String currency;

}