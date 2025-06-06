package com.phoenix.productinventory.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import com.phoenix.productinventory.mapper.ProductMapper;
import com.phoenix.productinventory.model.Category;
import com.phoenix.productinventory.model.Product;
import com.phoenix.productinventory.repository.ProductRepository;
import java.math.BigDecimal;
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

class ProductServiceImplTest {

  @Mock private ProductRepository productRepository;
  @Mock private ProductMapper productMapper;
  @Mock private CategoryService categoryService;
  @InjectMocks private ProductServiceImpl productService;

  private Product product;
  private ProductRequestDto requestDto;
  private ProductResponseDto responseDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    product = new Product(1L, "Test", "Desc", BigDecimal.valueOf(10), 5, 0, null);
    requestDto = new ProductRequestDto("Test", "Desc", BigDecimal.valueOf(10), 5);
    responseDto = new ProductResponseDto(1L, "Test", "Desc", BigDecimal.valueOf(10), 5, null, 0);
  }

  @Test
  @DisplayName("Given valid product request when createProduct then returns created product")
  void givenValidProductRequest_whenCreateProduct_thenReturnsCreatedProduct() {
    when(productMapper.toEntity(requestDto)).thenReturn(product);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toDto(product)).thenReturn(responseDto);

    ProductResponseDto result = productService.createProduct(requestDto);

    assertThat(result).isNotNull().extracting(ProductResponseDto::getName).isEqualTo("Test");
    verify(productRepository).save(product);
  }

  @Test
  @DisplayName("Given valid page request when getAllProducts then returns product list")
  void givenPageRequest_whenGetAllProducts_thenReturnsProductList() {
    Page<Product> page = new PageImpl<>(List.of(product));
    when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
    when(productMapper.toDto(product)).thenReturn(responseDto);

    Specification<Product> spec = Specification.where(null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ProductResponseDto> result = productService.getAllProducts(spec, pageable);

    assertThat(result.getContent()).hasSize(1);
  }

  @Test
  @DisplayName("Given valid ID when getProductById then returns product")
  void givenValidId_whenGetProductById_thenReturnsProduct() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productMapper.toDto(product)).thenReturn(responseDto);

    ProductResponseDto result = productService.getProductById(1L);

    assertThat(result).isNotNull().extracting(ProductResponseDto::getId).isEqualTo(1L);
  }

  @Test
  @DisplayName("Given invalid ID when getProductById then throws ResourceNotFoundException")
  void givenInvalidId_whenGetProductById_thenThrowsResourceNotFoundException() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.getProductById(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Given valid ID and request when updateProduct then returns updated product")
  void givenValidIdAndRequest_whenUpdateProduct_thenReturnsUpdatedProduct() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    doNothing().when(productMapper).updateEntityFromDto(requestDto, product);
    when(productRepository.save(product)).thenReturn(product);
    when(productMapper.toDto(product)).thenReturn(responseDto);

    ProductResponseDto result = productService.updateProduct(1L, requestDto);

    assertThat(result).isNotNull().extracting(ProductResponseDto::getId).isEqualTo(1L);
  }

  @Test
  @DisplayName("Given invalid ID when updateProduct then throws ResourceNotFoundException")
  void givenInvalidId_whenUpdateProduct_thenThrowsResourceNotFoundException() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.updateProduct(1L, requestDto))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName(
      "Given valid ID and request when updateProduct but optimistic locking fails then throws OptimisticLockingFailureException")
  void
      givenValidIdAndRequest_whenUpdateProductButOptimisticLockingFails_thenThrowsOptimisticLockingFailureException() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    doNothing().when(productMapper).updateEntityFromDto(requestDto, product);
    when(productRepository.save(product))
        .thenThrow(new OptimisticLockingFailureException("Any internal message"));

    assertThatThrownBy(() -> productService.updateProduct(1L, requestDto))
        .isInstanceOf(OptimisticLockingFailureException.class)
        .hasMessageContaining("Product was updated by another transaction.");

    verify(productRepository).save(product);
  }

  @Test
  @DisplayName("Given valid ID when deleteProduct then deletes product")
  void givenValidId_whenDeleteProduct_thenDeletesProduct() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));

    productService.deleteProduct(1L);

    verify(productRepository).delete(product);
  }

  @Test
  @DisplayName("Given invalid ID when deleteProduct then throws ResourceNotFoundException")
  void givenInvalidId_whenDeleteProduct_thenThrowsResourceNotFoundException() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.deleteProduct(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Given valid product and category IDs when assignCategory then assigns category")
  void givenValidProductAndCategoryIds_whenAssignCategory_thenAssignsCategory() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    Category category = new Category(2L, "TestCategory", "Desc", 0L, null);
    when(categoryService.getCategoryEntityById(2L)).thenReturn(category);
    when(productRepository.save(any(Product.class))).thenReturn(product);
    when(productMapper.toDto(any(Product.class))).thenReturn(responseDto);

    ProductResponseDto result = productService.assignCategory(1L, 2L);

    assertThat(result).isNotNull();
    verify(productRepository).save(product);
  }

  @Test
  @DisplayName("Given invalid product ID when assignCategory then throws ResourceNotFoundException")
  void givenInvalidProductId_whenAssignCategory_thenThrowsException() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.assignCategory(1L, 2L))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  @DisplayName("Given valid product ID when removeCategory then removes category")
  void givenValidProductId_whenRemoveCategory_thenRemovesCategory() {
    when(productRepository.findById(1L)).thenReturn(Optional.of(product));
    when(productRepository.save(any(Product.class))).thenReturn(product);
    when(productMapper.toDto(any(Product.class))).thenReturn(responseDto);

    ProductResponseDto result = productService.removeCategory(1L);

    assertThat(result).isNotNull();
    verify(productRepository).save(product);
  }

  @Test
  @DisplayName("Given invalid product ID when removeCategory then throws ResourceNotFoundException")
  void givenInvalidProductId_whenRemoveCategory_thenThrowsException() {
    when(productRepository.findById(1L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> productService.removeCategory(1L))
        .isInstanceOf(ResourceNotFoundException.class);
  }
}
