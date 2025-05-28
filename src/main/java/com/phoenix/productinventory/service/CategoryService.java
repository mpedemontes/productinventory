package com.phoenix.productinventory.service;

import com.phoenix.productinventory.dto.CategoryRequestDto;
import com.phoenix.productinventory.dto.CategoryResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import com.phoenix.productinventory.model.Category;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/** Service interface for managing categories. */
public interface CategoryService {
  /**
   * Creates a new category.
   *
   * @param requestDto The category details
   * @return The created category
   */
  CategoryResponseDto createCategory(CategoryRequestDto requestDto);

  /**
   * Retrieves a paginated list of categories matching the given filters.
   *
   * @param spec Specification for filtering categories (can be null).
   * @param pageable Pagination and sorting information.
   * @return A paginated list of matching categories.
   */
  Page<CategoryResponseDto> getAllCategories(Specification<Category> spec, Pageable pageable);

  /**
   * Retrieves a category by its ID.
   *
   * @param id The category ID.
   * @return The category if found.
   * @throws ResourceNotFoundException If no category with the given ID exists.
   */
  CategoryResponseDto getCategoryById(Long id);

  /**
   * Updates an existing category.
   *
   * @param id The ID of the category to update.
   * @param requestDto The new details for the category.
   * @return The updated category.
   * @throws ResourceNotFoundException If no category with the given ID exists.
   * @throws OptimisticLockingFailureException If concurrent modification is detected.
   */
  CategoryResponseDto updateCategory(Long id, CategoryRequestDto requestDto);

  /**
   * Deletes a category by its ID.
   *
   * @param id The category ID.
   * @throws ResourceNotFoundException If no category with the given ID exists.
   */
  void deleteCategory(Long id);

  /**
   * Retrieves a category entity by its ID.
   *
   * @param id the ID of the category
   * @return the category entity
   * @throws ResourceNotFoundException if category is not found
   */
  Category getCategoryEntityById(Long id);
}
