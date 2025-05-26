package com.phoenix.productinventory.repository;

import com.phoenix.productinventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository interface for Product entity persistence. */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {}
