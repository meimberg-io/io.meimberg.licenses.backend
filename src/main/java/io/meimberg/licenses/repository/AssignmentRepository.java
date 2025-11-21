package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.Assignment;
import io.meimberg.licenses.domain.AssignmentStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
  List<Assignment> findByUserId(UUID userId);
  List<Assignment> findByProductVariantId(UUID variantId);
  long countByProductVariantIdAndStatus(UUID variantId, AssignmentStatus status);
  boolean existsByUserIdAndProductVariantIdAndStatus(UUID userId, UUID productVariantId, AssignmentStatus status);
}


