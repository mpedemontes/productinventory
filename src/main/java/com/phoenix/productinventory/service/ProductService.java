package com.phoenix.productinventory.service;

import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import com.phoenix.productinventory.model.Product;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/** Service interface for managing products. */
public interface ProductService {

  /**
   * Creates a new product.
   *
   * @param requestDto The product details.
   * @return The created product.
   */
  ProductResponseDto createProduct(ProductRequestDto requestDto);

  /**
   * Retrieves a paginated list of products matching the given filters.
   *
   * @param spec Specification for filtering products (can be null).
   * @param pageable Pagination and sorting information.
   * @return A paginated list of matching products.
   */
  Page<ProductResponseDto> getAllProducts(Specification<Product> spec, Pageable pageable);

  /**
   * Retrieves a product by its ID.
   *
   * @param id The product ID.
   * @return The product if found.
   * @throws ResourceNotFoundException If no product with the given ID exists.
   */
  ProductResponseDto getProductById(Long id);

  /**
   * Updates an existing product.
   *
   * @param id The ID of the product to update.
   * @param requestDto The new details for the product.
   * @return The updated product.
   * @throws ResourceNotFoundException If no product with the given ID exists.
   * @throws OptimisticLockingFailureException If concurrent modification is detected.
   */
  ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);

  /**
   * Deletes a product by its ID.
   *
   * @param id The product ID.
   * @throws ResourceNotFoundException If no product with the given ID exists.
   */
  void deleteProduct(Long id);

  /**
   * Assigns a category to the specified product. Existing category associations will be replaced
   * with the provided category.
   *
   * @param productId the ID of the product
   * @param categoryId the ID of the category to assign
   * @return the updated product
   * @throws ResourceNotFoundException if the product or any category is not found
   */
  ProductResponseDto assignCategory(Long productId, Long categoryId);

  /**
   * Removes the category from the specified product.
   *
   * @param productId the ID of the product
   * @return the updated product
   * @throws ResourceNotFoundException if the product is not found
   */
  ProductResponseDto removeCategory(Long productId);
}
