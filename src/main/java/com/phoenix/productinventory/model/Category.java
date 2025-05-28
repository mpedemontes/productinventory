package com.phoenix.productinventory.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a category for products in the inventory system. Each category can have multiple
 * products.
 *
 * <p>Includes optimistic locking via the {@code version} field to handle concurrent updates.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

  /** Unique identifier for the category. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Name of the category. */
  @Column(nullable = false)
  @NotBlank(message = "Category name is required")
  private String name;

  /** Optional category description. */
  private String description;

  /** Version field used for optimistic locking. */
  @Version private Long version;

  /**
   * List of products associated with this category. Represents the one-to-many relationship between
   * Category and Product. Each category can have multiple products.
   */
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Product> products;
}
