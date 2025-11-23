package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
  List<Assignment> findByUserId(UUID userId);
  List<Assignment> findByProductVariantId(UUID variantId);
  long countByProductVariantId(UUID variantId);
  boolean existsByUserIdAndProductVariantId(UUID userId, UUID productVariantId);
  Page<Assignment> findAll(Pageable pageable);
  Page<Assignment> findByUserId(UUID userId, Pageable pageable);
  Page<Assignment> findByProductVariantId(UUID productVariantId, Pageable pageable);
  Page<Assignment> findByUserIdAndProductVariantId(UUID userId, UUID productVariantId, Pageable pageable);
}


