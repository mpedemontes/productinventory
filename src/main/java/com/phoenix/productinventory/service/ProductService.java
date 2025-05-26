package com.phoenix.productinventory.service;

import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
   * Retrieves a paginated list of products.
   *
   * @param pageable The pagination information.
   * @return A page of products.
   */
  Page<ProductResponseDto> getAllProducts(Pageable pageable);

  /**
   * Retrieves a product by its ID.
   *
   * @param id The product ID.
   * @return The product if found.
   * @throws ResourceNotFoundException if the product is not found.
   */
  ProductResponseDto getProductById(Long id);

  /**
   * Updates an existing product.
   *
   * @param id The product ID.
   * @param requestDto The updated product details.
   * @return The updated product.
   * @throws ResourceNotFoundException if the product is not found.
   */
  ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);

  /**
   * Deletes a product by its ID.
   *
   * @param id The product ID.
   * @throws ResourceNotFoundException if the product is not found.
   */
  void deleteProduct(Long id);
}
