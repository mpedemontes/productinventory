package com.phoenix.productinventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object used to receive product data from the client when creating or updating a
 * product.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {

  /** Name of the product. Cannot be blank. */
  @NotBlank(message = "Product name is required")
  private String name;

  /** Optional description of the product. */
  private String description;

  /** Price of the product. Must be greater than 0. */
  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
  private BigDecimal price;

  /** Quantity of the product in stock. Must be zero or positive. */
  @NotNull(message = "Quantity is required")
  @Min(value = 0, message = "Quantity must be zero or positive")
  private Integer quantity;
}
