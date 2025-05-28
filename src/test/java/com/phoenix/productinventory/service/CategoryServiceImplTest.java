package com.phoenix.productinventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.phoenix.productinventory.dto.CategoryRequestDto;
import com.phoenix.productinventory.dto.CategoryResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import com.phoenix.productinventory.mapper.CategoryMapper;
import com.phoenix.productinventory.model.Category;
import com.phoenix.productinventory.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

class CategoryServiceImplTest {

  @Mock private CategoryRepository categoryRepository;
  @Mock private CategoryMapper categoryMapper;
  @InjectMocks private CategoryServiceImpl categoryService;

  private Category category;
  private CategoryRequestDto requestDto;
  private CategoryResponseDto responseDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    category = new Category(1L, "TestCategory", "Desc", 0L, null);
    requestDto = new CategoryRequestDto("TestCategory", "Desc");
    responseDto = new CategoryResponseDto(1L, "TestCategory", "Desc");
  }

  @Test
  @DisplayName("Given valid request when createCategory then returns created category")
  void givenValidRequest_whenCreateCategory_thenReturnsCreatedCategory() {
    when(categoryMapper.toEntity(requestDto)).thenReturn(category);
    when(categoryRepository.save(category)).thenReturn(category);
    when(categoryMapper.toDto(category)).thenReturn(responseDto);

    CategoryResponseDto result = categoryService.createCategory(requestDto);

    assertThat(result).isNotNull().extracting(CategoryResponseDto::getId).isEqualTo(1L);
    verify(categoryRepository).save(category);
  }

  @Test
  @DisplayName("Given valid ID when getCategoryById then returns category")
  void givenValidId_whenGetCategoryById_thenReturnsCategory() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    when(categoryMapper.toDto(category)).thenReturn(responseDto);

    CategoryResponseDto result = categoryService.getCategoryById(1L);

    assertThat(result).isNotNull().extracting(CategoryResponseDto::getId).isEqualTo(1L);
  }

  @Test
  @DisplayName("Given invalid ID when getCategoryById then throws exception")
  void givenInvalidId_whenGetCategoryById_thenThrowsException() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> categoryService.getCategoryById(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Given valid ID when getCategoryEntityById then returns category")
  void givenValidId_whenGetCategoryEntityById_thenReturnsCategory() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    Category result = categoryService.getCategoryEntityById(1L);

    assertThat(result).isNotNull().extracting(Category::getId).isEqualTo(1L);
  }

  @Test
  @DisplayName("Given invalid ID when getCategoryEntityById then throws exception")
  void givenInvalidId_whenGetCategoryEntityById_thenThrowsException() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> categoryService.getCategoryEntityById(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Given valid page request when getAllCategories then returns category list")
  void givenPageRequest_whenGetAllProducts_thenReturnsProductList() {
    Page<Category> page = new PageImpl<>(List.of(category));
    when(categoryRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(page);
    when(categoryMapper.toDto(category)).thenReturn(responseDto);

    Specification<Category> spec = Specification.where(null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<CategoryResponseDto> result = categoryService.getAllCategories(spec, pageable);

    assertThat(result.getContent()).hasSize(1);
  }

  @Test
  @DisplayName("Given valid ID and request when updateCategory then returns updated category")
  void givenValidIdAndRequest_whenUpdateCategory_thenReturnsUpdatedCategory() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    doNothing().when(categoryMapper).updateEntityFromDto(requestDto, category);
    when(categoryRepository.save(category)).thenReturn(category);
    when(categoryMapper.toDto(category)).thenReturn(responseDto);

    CategoryResponseDto result = categoryService.updateCategory(1L, requestDto);

    assertThat(result).isNotNull().extracting(CategoryResponseDto::getId).isEqualTo(1L);
  }

  @Test
  @DisplayName("Given invalid ID when updateCategory then throws exception")
  void givenInvalidId_whenUpdateCategory_thenThrowsException() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> categoryService.updateCategory(1L, requestDto))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName(
      "Given valid ID and request when updateCategory but optimistic locking fails then throws OptimisticLockingFailureException")
  void
      givenValidIdAndRequest_whenUpdateCategoryButOptimisticLockingFails_thenThrowsOptimisticLockingFailureException() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
    doNothing().when(categoryMapper).updateEntityFromDto(requestDto, category);
    when(categoryRepository.save(category))
        .thenThrow(new OptimisticLockingFailureException("Any internal message"));

    assertThatThrownBy(() -> categoryService.updateCategory(1L, requestDto))
        .isInstanceOf(OptimisticLockingFailureException.class)
        .hasMessageContaining("Category was updated by another transaction.");

    verify(categoryRepository).save(category);
  }

  @Test
  @DisplayName("Given valid ID when deleteCategory then deletes category")
  void givenValidId_whenDeleteCategory_thenDeletesCategory() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

    categoryService.deleteCategory(1L);

    verify(categoryRepository).delete(category);
  }

  @Test
  @DisplayName("Given invalid ID when deleteCategory then throws exception")
  void givenInvalidId_whenDeleteCategory_thenThrowsException() {
    when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> categoryService.deleteCategory(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
