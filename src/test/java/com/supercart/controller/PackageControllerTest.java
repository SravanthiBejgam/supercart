package com.supercart.controller;

import com.supercart.dto.PackageResponse;
import com.supercart.model.Product;
import com.supercart.service.PackageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PackageController.class)
class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PackageService packageService;

    @Test
    void createPackage_ShouldReturnCreated() throws Exception {
        PackageResponse response = createSampleResponse("USD");
        when(packageService.createPackage(any())).thenReturn(response);

        mockMvc.perform(post("/api/packages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Test Package",
                        "description": "Test Description",
                        "productIds": ["VqKb4tyj9V6i"]
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.price").value(1149));
    }

    @Test
    void getPackage_ShouldReturnPackage() throws Exception {
        PackageResponse response = createSampleResponse("EUR");
        when(packageService.getPackage(anyString(), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/packages/1?currency=EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void updatePackage_ShouldReturnUpdatedPackage() throws Exception {
        PackageResponse response = createSampleResponse("GBP");
        when(packageService.updatePackage(anyString(), any(), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/packages/1?currency=GBP")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Updated Package",
                        "description": "New Description",
                        "productIds": ["VqKb4tyj9V6i"]
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("GBP"));
    }

    @Test
    void deletePackage_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/packages/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllPackages_ShouldReturnList() throws Exception {
        List<PackageResponse> responses = Arrays.asList(
                createSampleResponse("USD"),
                createSampleResponse("EUR")
        );
        when(packageService.listAllPackages(anyString())).thenReturn(responses);

        mockMvc.perform(get("/api/packages?currency=USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    private PackageResponse createSampleResponse(String currency) {

        Product product = new Product(
                "VqKb4tyj9V6i",
                "Shield",
                BigDecimal.valueOf(1149));

        return new PackageResponse(
                "1",
                "Test Package",
                "Test Description",
                List.of(product),
                BigDecimal.valueOf(1149),
                currency
        );
    }
}