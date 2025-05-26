package com.phoenix.productinventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
 * Entity that represents a product in the inventory.
 *
 * <p>Includes optimistic locking via the {@code version} field to handle concurrent updates.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

  /** Unique identifier for the product. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Name of the product. */
  @Column(nullable = false)
  @NotBlank(message = "Product name is required")
  private String name;

  /** Optional product description. */
  private String description;

  /** Product price. Must be greater than zero. */
  @Column(nullable = false)
  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
  private BigDecimal price;

  /** Quantity in stock. Must be zero or greater. */
  @Column(nullable = false)
  @NotNull(message = "Quantity is required")
  @Min(value = 0, message = "Quantity must be zero or positive")
  private Integer quantity;

  /** Version field used for optimistic locking. */
  @Version private Integer version;
}
