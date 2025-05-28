package com.phoenix.productinventory.service;

import com.phoenix.productinventory.dto.CategoryRequestDto;
import com.phoenix.productinventory.dto.CategoryResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import com.phoenix.productinventory.mapper.CategoryMapper;
import com.phoenix.productinventory.model.Category;
import com.phoenix.productinventory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of CategoryService interface */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private static final String CATEGORY_NOT_FOUND = "Category not found with id %s";

  private final CategoryRepository repository;
  private final CategoryMapper mapper;

  @Override
  @Transactional
  public CategoryResponseDto createCategory(CategoryRequestDto categoryDto) {
    Category category = mapper.toEntity(categoryDto);
    Category saved = repository.save(category);
    return mapper.toDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CategoryResponseDto> getAllCategories(
      Specification<Category> spec, Pageable pageable) {
    return repository.findAll(spec, pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryResponseDto getCategoryById(Long id) {
    Category category =
        repository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, id)));
    return mapper.toDto(category);
  }

  @Override
  @Transactional
  public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryDto) {
    Category existingCategory =
        repository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, id)));

    try {
      mapper.updateEntityFromDto(categoryDto, existingCategory);
      Category updatedCategory = repository.save(existingCategory);
      return mapper.toDto(updatedCategory);
    } catch (OptimisticLockingFailureException e) {
      throw new OptimisticLockingFailureException("Category was updated by another transaction.");
    }
  }

  @Override
  @Transactional
  public void deleteCategory(Long id) {
    Category category =
        repository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(String.format(CATEGORY_NOT_FOUND, id)));
    repository.delete(category);
  }

  @Override
  @Transactional(readOnly = true)
  public Category getCategoryEntityById(Long id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id));
  }
}
