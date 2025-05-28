package com.phoenix.productinventory.controller;

import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.model.Product;
import com.phoenix.productinventory.service.ProductService;
import com.phoenix.productinventory.spcification.ProductSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for managing products. */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "API for managing products in the inventory")
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
      summary = "Get products with optional filters, pagination, and sorting",
      description =
          "Retrieves products optionally filtered by name, price, and quantity, with support for pagination and sorting. "
              + "Sorting can be applied by adding 'sort' query parameters (e.g., sort=price,asc).",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Products retrieved successfully",
            content = @Content(mediaType = "application/json"))
      })
  @GetMapping
  public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
      @Parameter(description = "Product name filter (optional)") @RequestParam(required = false)
          String name,
      @Parameter(description = "Minimum price filter (optional)") @RequestParam(required = false)
          BigDecimal minPrice,
      @Parameter(description = "Maximum price filter (optional)") @RequestParam(required = false)
          BigDecimal maxPrice,
      @Parameter(description = "Minimum quantity filter (optional)") @RequestParam(required = false)
          Integer minQuantity,
      @Parameter(description = "Maximum quantity filter (optional)") @RequestParam(required = false)
          Integer maxQuantity,
      @ParameterObject Pageable pageable) {

    Specification<Product> spec =
        Specification.where(ProductSpecification.hasName(name))
            .and(ProductSpecification.hasMinPrice(minPrice))
            .and(ProductSpecification.hasMaxPrice(maxPrice))
            .and(ProductSpecification.hasMinQuantity(minQuantity))
            .and(ProductSpecification.hasMaxQuantity(maxQuantity));

    return ResponseEntity.ok(productService.getAllProducts(spec, pageable));
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

  @Operation(
      summary = "Assigns a category to a product",
      description = "Assigns a category to the specified product.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Product updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product or category not found")
      })
  @PutMapping("/{productId}/category/{categoryId}")
  public ResponseEntity<ProductResponseDto> assignCategoriesToProduct(
      @Parameter(description = "Product ID") @PathVariable Long productId,
      @Parameter(description = "Category ID") @PathVariable Long categoryId) {
    ProductResponseDto updatedProduct = productService.assignCategory(productId, categoryId);
    return ResponseEntity.ok(updatedProduct);
  }

  @Operation(
      summary = "Remove category from a product",
      description = "Removes the category from the specified product.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Category removed successfully from product",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
      })
  @DeleteMapping("/{productId}/category")
  public ResponseEntity<ProductResponseDto> removeCategoriesFromProduct(
      @Parameter(description = "Product ID") @PathVariable Long productId) {
    return ResponseEntity.ok(productService.removeCategory(productId));
  }
}
