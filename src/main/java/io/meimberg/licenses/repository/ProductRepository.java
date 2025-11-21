package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  Optional<Product> findByKey(String key);
}


