package com.phoenix.productinventory.controller;

import com.phoenix.productinventory.dto.CategoryRequestDto;
import com.phoenix.productinventory.dto.CategoryResponseDto;
import com.phoenix.productinventory.model.Category;
import com.phoenix.productinventory.service.CategoryService;
import com.phoenix.productinventory.spcification.CategorySpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

/** REST controller for managing categories. */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "API for managing categories")
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(
      summary = "Create a new category",
      description = "Creates a new category with the given details.",
      responses = {
        @ApiResponse(
            responseCode = "201",
            description = "Category created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
      })
  @PostMapping
  public ResponseEntity<CategoryResponseDto> createCategory(
      @Valid @RequestBody CategoryRequestDto dto) {
    CategoryResponseDto response = categoryService.createCategory(dto);
    return ResponseEntity.status(201).body(response);
  }

  @Operation(
      summary = "Get a category by ID",
      description = "Retrieves a category by its ID.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Category retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Category not found")
      })
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> getCategoryById(
      @Parameter(description = "Category ID") @PathVariable Long id) {
    CategoryResponseDto response = categoryService.getCategoryById(id);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Get categories with optional filters, pagination, and sorting",
      description =
          "Retrieves products optionally filtered by name, price, and quantity, with support for pagination and sorting. "
              + "Sorting can be applied by adding 'sort' query parameters (e.g., sort=name,asc).",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Categories retrieved successfully",
            content = @Content(mediaType = "application/json"))
      })
  @GetMapping
  public ResponseEntity<Page<CategoryResponseDto>> getAllCategories(
      @Parameter(description = "Category name filter (optional)") @RequestParam(required = false)
          String name,
      @ParameterObject Pageable pageable) {

    Specification<Category> spec = Specification.where(CategorySpecification.hasName(name));

    return ResponseEntity.ok(categoryService.getAllCategories(spec, pageable));
  }

  @Operation(
      summary = "Update a category",
      description = "Updates a category by its ID.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "Category updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
      })
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> updateCategory(
      @Parameter(description = "Category ID") @PathVariable Long id,
      @Valid @RequestBody CategoryRequestDto dto) {
    CategoryResponseDto response = categoryService.updateCategory(id, dto);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Delete a category",
      description = "Deletes a category by its ID.",
      responses = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
      })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCategory(
      @Parameter(description = "Category ID") @PathVariable Long id) {
    categoryService.deleteCategory(id);
    return ResponseEntity.noContent().build();
  }
}
