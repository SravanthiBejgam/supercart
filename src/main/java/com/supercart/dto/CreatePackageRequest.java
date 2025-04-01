package com.supercart.dto;

import java.util.List;

public record CreatePackageRequest (
     String name,
     String description,
     List<String> productIds
){}