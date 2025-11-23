package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
  Optional<ProductCategory> findByName(String name);
}

