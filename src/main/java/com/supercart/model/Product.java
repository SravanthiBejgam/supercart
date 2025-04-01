package com.supercart.model;

import lombok.Data;

import java.math.BigDecimal;


public record Product (
     String id,
     String name,
     BigDecimal usdPrice
){}