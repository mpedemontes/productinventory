package com.phoenix.productinventory.spcification;

import com.phoenix.productinventory.model.Product;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public class ProductSpecification {

  public static Specification<Product> hasName(String name) {
    return (root, query, cb) ->
        name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }

  public static Specification<Product> hasMinPrice(BigDecimal minPrice) {
    return (root, query, cb) ->
        minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
  }

  public static Specification<Product> hasMaxPrice(BigDecimal maxPrice) {
    return (root, query, cb) ->
        maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
  }

  public static Specification<Product> hasMinQuantity(Integer minQuantity) {
    return (root, query, cb) ->
        minQuantity == null ? null : cb.greaterThanOrEqualTo(root.get("quantity"), minQuantity);
  }

  public static Specification<Product> hasMaxQuantity(Integer maxQuantity) {
    return (root, query, cb) ->
        maxQuantity == null ? null : cb.lessThanOrEqualTo(root.get("quantity"), maxQuantity);
  }
}
