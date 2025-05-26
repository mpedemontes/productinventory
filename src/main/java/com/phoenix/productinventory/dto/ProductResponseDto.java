package com.phoenix.productinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/** Data Transfer Object used to send product data to the client. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

  /** Unique identifier of the product. */
  private Long id;

  /** Name of the product. */
  private String name;

  /** Description of the product. */
  private String description;

  /** Price of the product. */
  private BigDecimal price;

  /** Quantity in stock. */
  private Integer quantity;

  /** Version used for optimistic locking. */
  private Integer version;
}
