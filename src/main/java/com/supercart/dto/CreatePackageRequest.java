package com.supercart.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreatePackageRequest {
    private String name;
    private String description;
    private List<String> productIds;
}