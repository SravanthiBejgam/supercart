package com.supercart.service;

import com.supercart.dto.CreatePackageRequest;
import com.supercart.dto.PackageResponse;
import com.supercart.exception.InvalidProductException;
import com.supercart.exception.PackageNotFoundException;
import com.supercart.model.PackageEntity;
import com.supercart.model.Product;
import com.supercart.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageService {

    private final PackageRepository packageRepository;
    private final ProductServiceClient productServiceClient;
    private final CurrencyConverter currencyConverter;

    public PackageResponse createPackage(CreatePackageRequest request) {
        List<String> productIds = request.getProductIds();
        List<Product> allProducts = productServiceClient.getAllProducts();

        List<Product> productsInPackage = productIds.stream()
                .map(productId -> allProducts.stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElseThrow(() -> new InvalidProductException("Invalid product ID: " + productId)))
                .collect(Collectors.toList());

        BigDecimal totalUsd = productsInPackage.stream()
                .map(Product::getUsdPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PackageEntity entity = new PackageEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setProductIds(productIds);
        entity.setPriceUSD(totalUsd);

        PackageEntity savedEntity = packageRepository.save(entity);
        return convertToPackageResponse(savedEntity, "USD");
    }

    public PackageResponse getPackage(String id, String currency) {
        PackageEntity entity = packageRepository.findById(id)
                .orElseThrow(() -> new PackageNotFoundException(id));

        List<Product> allProducts = productServiceClient.getAllProducts();
        List<Product> products = entity.getProductIds().stream()
                .map(productId -> allProducts.stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId)))
                .collect(Collectors.toList());

        BigDecimal convertedPrice = currencyConverter.convertUsdToCurrency(entity.getPriceUSD(), currency == null ? "USD" : currency);

        return new PackageResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                products,
                convertedPrice,
                currency == null ? "USD" : currency
        );
    }

    public PackageResponse updatePackage(String id, CreatePackageRequest request, String currency) {
        PackageEntity existingEntity = packageRepository.findById(id)
                .orElseThrow(() -> new PackageNotFoundException(id));

        List<String> productIds = request.getProductIds();
        List<Product> allProducts = productServiceClient.getAllProducts();

        List<Product> productsInPackage = productIds.stream()
                .map(productId -> allProducts.stream()
                        .filter(p -> p.getId().equals(productId))
                        .findFirst()
                        .orElseThrow(() -> new InvalidProductException("Invalid product ID: " + productId)))
                .collect(Collectors.toList());

        BigDecimal totalUsd = productsInPackage.stream()
                .map(Product::getUsdPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        existingEntity.setName(request.getName());
        existingEntity.setDescription(request.getDescription());
        existingEntity.setProductIds(productIds);
        existingEntity.setPriceUSD(totalUsd);

        PackageEntity updatedEntity = packageRepository.save(existingEntity);
        return convertToPackageResponse(updatedEntity, currency != null ? currency : "USD");
    }

    public void deletePackage(String id) {
        if (!packageRepository.existsById(id)) {
            throw new PackageNotFoundException(id);
        }
        packageRepository.deleteById(id);
    }

    public List<PackageResponse> listAllPackages(String currency) {
        String targetCurrency = currency != null ? currency : "USD";
        return packageRepository.findAll().stream()
                .map(entity -> convertToPackageResponse(entity, targetCurrency))
                .collect(Collectors.toList());
    }

    private PackageResponse convertToPackageResponse(PackageEntity entity, String currency) {
        return new PackageResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                productServiceClient.getAllProducts().stream()
                        .filter(p -> entity.getProductIds().contains(p.getId()))
                        .collect(Collectors.toList()),
                currencyConverter.convertUsdToCurrency(entity.getPriceUSD(), currency),
                currency
        );
    }
}