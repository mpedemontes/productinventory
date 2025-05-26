package com.phoenix.productinventory.service;

import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.exception.ResourceNotFoundException;
import com.phoenix.productinventory.mapper.ProductMapper;
import com.phoenix.productinventory.model.Product;
import com.phoenix.productinventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of ProductService interface */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private static final String PRODUCT_NOT_FOUND = "Product not found with id %s";

  private final ProductRepository repository;
  private final ProductMapper mapper;

  @Override
  @Transactional
  public ProductResponseDto createProduct(ProductRequestDto productDto) {
    Product product = mapper.toEntity(productDto);
    Product saved = repository.save(product);
    return mapper.toDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductResponseDto getProductById(Long id) {
    Product product =
        repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));
    return mapper.toDto(product);
  }

  @Override
  @Transactional
  public ProductResponseDto updateProduct(Long id, ProductRequestDto productDto) {
    Product existingProduct =
        repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));

    try {
      mapper.updateEntityFromDto(productDto, existingProduct);
      Product updatedProduct = repository.save(existingProduct);
      return mapper.toDto(updatedProduct);
    } catch (OptimisticLockingFailureException e) {
      throw new OptimisticLockingFailureException("Product was updated by another transaction.");
    }
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) {
    Product product =
        repository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(String.format(PRODUCT_NOT_FOUND, id)));
    repository.delete(product);
  }
}
