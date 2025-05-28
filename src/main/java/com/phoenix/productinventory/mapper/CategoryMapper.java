package com.phoenix.productinventory.mapper;

import com.phoenix.productinventory.dto.CategoryRequestDto;
import com.phoenix.productinventory.dto.CategoryResponseDto;
import com.phoenix.productinventory.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

  /**
   * Converts a CategoryRequestDto into a Category entity.
   *
   * @param dto the category request data
   * @return the corresponding Category entity
   */
  Category toEntity(CategoryRequestDto dto);

  /**
   * Converts a Category entity into a CategoryResponseDto
   *
   * @param entity the Category entity
   * @return the corresponding CategoryResponseDto
   */
  CategoryResponseDto toDto(Category entity);

  /**
   * Converts a list of Category entities into a list of CategoryResponseDto
   *
   * @param entities the list of Category entities
   * @return the corresponding list of CategoryResponseDto
   */
  List<CategoryResponseDto> toDtoList(List<Category> entities);

  /**
   * Updates an existing Category entity with the values from a CategoryRequestDto. Ignores null
   * values from the DTO (they won't overwrite existing values).
   *
   * @param dto the DTO with updated values
   * @param entity the entity to be updated
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromDto(CategoryRequestDto dto, @MappingTarget Category entity);
}
