package com.phoenix.productinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Data Transfer Object used to send category data to the client. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

  /** Unique identifier of the category. */
  private Long id;

  /** Name of the category. */
  private String name;

  /** Description of the category. */
  private String description;
}
