package com.phoenix.productinventory.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object used to receive category data from the client when creating or updating a
 * category.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {

  /** Name of the category. Cannot be blank. */
  @Column(nullable = false)
  @NotBlank(message = "Category name is required")
  private String name;

  /** Optional description of the category. */
  private String description;
}
