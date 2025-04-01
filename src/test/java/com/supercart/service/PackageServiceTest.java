package com.supercart.service;

import com.supercart.dto.CreatePackageRequest;
import com.supercart.dto.PackageResponse;
import com.supercart.exception.InvalidProductException;
import com.supercart.exception.PackageNotFoundException;
import com.supercart.model.PackageEntity;
import com.supercart.model.Product;
import com.supercart.repository.PackageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    @Mock
    private CurrencyConverter currencyConverter;

    @InjectMocks
    private PackageService packageService;

    @Test
    void createPackage_WithValidProducts_ShouldReturnResponse() {
        // Arrange
        CreatePackageRequest request = new CreatePackageRequest(
                "Test Package",
                "Test Description",
                List.of("VqKb4tyj9V6i")  // Using record constructor
        );

        Product product = new Product(
                "VqKb4tyj9V6i",
                "Shield",
                BigDecimal.valueOf(1149)
        );

        when(productServiceClient.getAllProducts()).thenReturn(List.of(product));
        when(packageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(currencyConverter.convertUsdToCurrency(any(BigDecimal.class), eq("USD")))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        PackageResponse response = packageService.createPackage(request);

        // Assert
        assertNotNull(response.id());
        assertEquals(BigDecimal.valueOf(1149), response.price());
        verify(packageRepository).save(any());
    }

    @Test
    void createPackage_WithInvalidProduct_ShouldThrowException() {

        CreatePackageRequest request = new CreatePackageRequest(
                "Test Package",
                "Test Description",
                List.of("invalid-id")  // Using record constructor
        );
        
        when(productServiceClient.getAllProducts()).thenReturn(Collections.emptyList());

        assertThrows(InvalidProductException.class, () -> packageService.createPackage(request));
    }

    @Test
    void getPackage_ExistingId_ShouldReturnResponse() {
        // Arrange
        PackageEntity entity = createSampleEntity();
        when(packageRepository.findById("1")).thenReturn(Optional.of(entity));
        when(productServiceClient.getAllProducts()).thenReturn(List.of(createSampleProduct()));
        when(currencyConverter.convertUsdToCurrency(any(), anyString()))
                .thenReturn(BigDecimal.valueOf(1000));

        // Act
        PackageResponse response = packageService.getPackage("1", "EUR");

        // Assert
        assertEquals("1", response.id());
        assertEquals(BigDecimal.valueOf(1000), response.price());
    }

    @Test
    void getPackage_NonExistingId_ShouldThrowException() {
        when(packageRepository.findById("invalid")).thenReturn(Optional.empty());
        
        assertThrows(PackageNotFoundException.class, 
            () -> packageService.getPackage("invalid", "USD"));
    }

    @Test
    void updatePackage_ValidRequest_ShouldUpdateEntity() {
        // Arrange
        PackageEntity existingEntity = createSampleEntity();
        CreatePackageRequest request = new CreatePackageRequest(
                "Test Package",
                "Test Description",
                List.of("VqKb4tyj9V6i")  // Using record constructor
        );
        when(packageRepository.findById("1")).thenReturn(Optional.of(existingEntity));
        when(productServiceClient.getAllProducts()).thenReturn(List.of(createSampleProduct()));
        when(packageRepository.save(any())).thenReturn(existingEntity);

        // Act
        PackageResponse response = packageService.updatePackage("1", request, "EUR");

        // Assert
        verify(packageRepository).save(existingEntity);
        assertNotNull(response);
    }

    @Test
    void deletePackage_ExistingId_ShouldDeleteEntity() {
        when(packageRepository.existsById("1")).thenReturn(true);
        
        packageService.deletePackage("1");
        
        verify(packageRepository).deleteById("1");
    }

    @Test
    void listAllPackages_ShouldReturnAllConverted() {
        // Arrange
        PackageEntity entity = createSampleEntity();
        when(packageRepository.findAll()).thenReturn(List.of(entity));
        when(currencyConverter.convertUsdToCurrency(any(), anyString()))
                .thenReturn(BigDecimal.valueOf(1000));

        // Act
        List<PackageResponse> responses = packageService.listAllPackages("EUR");

        // Assert
        assertEquals(1, responses.size());
        assertEquals("EUR", responses.get(0).currency());
    }

    private PackageEntity createSampleEntity() {
        PackageEntity entity = new PackageEntity();
        entity.setId("1");
        entity.setProductIds(List.of("VqKb4tyj9V6i"));
        entity.setPriceUSD(BigDecimal.valueOf(1149));
        return entity;
    }

    private Product createSampleProduct() {
        return new Product(
                "VqKb4tyj9V6i",
                "Shield",
                BigDecimal.valueOf(1149)
        );
    }
}