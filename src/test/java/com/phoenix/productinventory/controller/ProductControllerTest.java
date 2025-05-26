package com.phoenix.productinventory.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import com.phoenix.productinventory.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

  private final ProductResponseDto responseDto =
      new ProductResponseDto(1L, "Test", "Desc", BigDecimal.valueOf(10), 5, 0);
  @Autowired private MockMvc mockMvc;
  @MockitoBean private ProductService productService;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName(
      "Given valid request when createProduct then returns created product with 201 status")
  void givenValidRequest_whenCreateProduct_thenReturnsCreatedProduct() throws Exception {
    ProductRequestDto request = new ProductRequestDto("Test", "Desc", BigDecimal.valueOf(10), 5);
    Mockito.when(productService.createProduct(any())).thenReturn(responseDto);

    mockMvc
        .perform(
            post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  @DisplayName("When getAllProducts then returns product list with 200 status")
  void whenGetAllProducts_thenReturnsProductList() throws Exception {
    Mockito.when(productService.getAllProducts(any()))
        .thenReturn(new PageImpl<>(List.of(responseDto)));

    mockMvc
        .perform(get("/products").param("page", "0").param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)));
  }

  @Test
  @DisplayName("Given valid ID when getProductById then returns product with 200 status")
  void givenValidId_whenGetProductById_thenReturnsProduct() throws Exception {
    Mockito.when(productService.getProductById(1L)).thenReturn(responseDto);

    mockMvc
        .perform(get("/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  @DisplayName("Given invalid ID when getProductById then returns 404 status")
  void givenInvalidId_whenGetProductById_thenReturns404() throws Exception {
    Mockito.when(productService.getProductById(1L))
        .thenThrow(new ResourceNotFoundException("Not found"));

    mockMvc.perform(get("/products/1")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName(
      "Given valid ID and request when updateProduct then returns updated product with 200 status")
  void givenValidIdAndRequest_whenUpdateProduct_thenReturnsUpdatedProduct() throws Exception {
    ProductRequestDto request =
        new ProductRequestDto("Updated", "Desc", BigDecimal.valueOf(20), 10);
    Mockito.when(productService.updateProduct(eq(1L), any())).thenReturn(responseDto);

    mockMvc
        .perform(
            put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));
  }

  @Test
  @DisplayName("Given valid ID when deleteProduct then returns 204 status")
  void givenValidId_whenDeleteProduct_thenReturnsNoContent() throws Exception {
    mockMvc.perform(delete("/products/1")).andExpect(status().isNoContent());
  }
}
