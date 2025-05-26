package com.phoenix.productinventory.controller;

import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for managing products. */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @Operation(
      summary = "Create a new product",
      description = "Creates a new product with the given details.",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Product created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
      })
  @PostMapping
  public ResponseEntity<ProductResponseDto> createProduct(
      @Valid @RequestBody ProductRequestDto productRequestDto) {
    return ResponseEntity.status(201).body(productService.createProduct(productRequestDto));
  }

  @Operation(
      summary = "Get all products",
      description = "Retrieves a paginated list of products.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content = @Content(mediaType = "application/json"))
      })
  @GetMapping
  public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
      @Parameter(description = "Pagination information") Pageable pageable) {
    return ResponseEntity.ok(productService.getAllProducts(pageable));
  }

  @Operation(
      summary = "Get a product by ID",
      description = "Retrieves a product by its ID.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Product retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getProductById(
      @Parameter(description = "Product ID") @PathVariable Long id) {
    ProductResponseDto response = productService.getProductById(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDto> updateProduct(
      @Parameter(description = "Product ID") @PathVariable Long id,
      @Valid @RequestBody ProductRequestDto requestDto) {
    ProductResponseDto response = productService.updateProduct(id, requestDto);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Delete a product",
      description = "Deletes a product by its ID.",
      responses = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(
      @Parameter(description = "Product ID") @PathVariable Long id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
