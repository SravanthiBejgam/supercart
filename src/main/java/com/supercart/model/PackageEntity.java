package com.supercart.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class PackageEntity {
    @Id
    private String id;
    private String name;
    private String description;
    @ElementCollection
    private List<String> productIds;
    private BigDecimal priceUSD;

}