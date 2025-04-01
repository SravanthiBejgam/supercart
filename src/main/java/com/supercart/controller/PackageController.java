package com.supercart.controller;

import com.supercart.dto.CreatePackageRequest;
import com.supercart.dto.PackageResponse;
import com.supercart.service.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

    @PostMapping
    public ResponseEntity<PackageResponse> createPackage(@RequestBody CreatePackageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packageService.createPackage(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageResponse> getPackage(@PathVariable String id, @RequestParam(required = false) String currency) {
        return ResponseEntity.ok(packageService.getPackage(id, currency));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageResponse> updatePackage(
            @PathVariable String id,
            @RequestBody CreatePackageRequest request,
            @RequestParam(required = false) String currency) {
        return ResponseEntity.ok(packageService.updatePackage(id, request, currency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable String id) {
        packageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PackageResponse>> getAllPackages(
            @RequestParam(required = false) String currency) {
        System.out.println("Hello==========>");
        return ResponseEntity.ok(packageService.listAllPackages(currency));
    }}