package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.Assignment;
import io.meimberg.licenses.domain.AssignmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
  List<Assignment> findByUserId(UUID userId);
  List<Assignment> findByProductVariantId(UUID variantId);
  long countByProductVariantIdAndStatus(UUID variantId, AssignmentStatus status);
  boolean existsByUserIdAndProductVariantIdAndStatus(UUID userId, UUID productVariantId, AssignmentStatus status);
  Page<Assignment> findAll(Pageable pageable);
  Page<Assignment> findByUserId(UUID userId, Pageable pageable);
  Page<Assignment> findByProductVariantId(UUID productVariantId, Pageable pageable);
  Page<Assignment> findByStatus(AssignmentStatus status, Pageable pageable);
  Page<Assignment> findByUserIdAndProductVariantId(UUID userId, UUID productVariantId, Pageable pageable);
  Page<Assignment> findByUserIdAndStatus(UUID userId, AssignmentStatus status, Pageable pageable);
  Page<Assignment> findByProductVariantIdAndStatus(UUID productVariantId, AssignmentStatus status, Pageable pageable);
  Page<Assignment> findByUserIdAndProductVariantIdAndStatus(UUID userId, UUID productVariantId, AssignmentStatus status, Pageable pageable);
}


