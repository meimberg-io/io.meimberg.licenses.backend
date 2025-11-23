package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  Optional<Product> findByKey(String key);
  List<Product> findByManufacturerId(UUID manufacturerId);
  List<Product> findByCategoryId(UUID categoryId);
  List<Product> findByManufacturerIdAndCategoryId(UUID manufacturerId, UUID categoryId);
}


