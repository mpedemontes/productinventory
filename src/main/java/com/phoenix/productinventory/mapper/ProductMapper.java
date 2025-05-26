package com.phoenix.productinventory.mapper;

import com.phoenix.productinventory.dto.ProductRequestDto;
import com.phoenix.productinventory.dto.ProductResponseDto;
import com.phoenix.productinventory.model.Product;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper interface to convert between Product entity and DTOs.
 *
 * <p>This mapper simplifies object transformations and ensures consistency across layers.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

  /**
   * Converts a ProductRequestDto into a Product entity.
   *
   * @param dto the product request data
   * @return the corresponding Product entity
   */
  Product toEntity(ProductRequestDto dto);

  /**
   * Converts a Product entity into a ProductResponseDto.
   *
   * @param entity the Product entity
   * @return the corresponding ProductResponseDto
   */
  ProductResponseDto toDto(Product entity);

  /**
   * Converts a list of Product entities into a list of ProductResponseDto.
   *
   * @param entities the list of Product entities
   * @return the corresponding list of ProductResponseDto
   */
  List<ProductResponseDto> toDtoList(List<Product> entities);

  /**
   * Updates an existing Product entity with the values from a ProductRequestDto. Ignores null
   * values from the DTO (they won't overwrite existing values).
   *
   * @param dto the DTO with updated values
   * @param entity the entity to be updated
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromDto(ProductRequestDto dto, @MappingTarget Product entity);
}
