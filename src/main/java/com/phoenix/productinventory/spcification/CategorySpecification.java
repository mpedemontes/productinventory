package com.phoenix.productinventory.spcification;

import com.phoenix.productinventory.model.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

  public static Specification<Category> hasName(String name) {
    return (root, query, cb) ->
        name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }
}
