package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.ProductVariant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
  List<ProductVariant> findByProductId(UUID productId);
  Optional<ProductVariant> findByProductIdAndKey(UUID productId, String key);
}


