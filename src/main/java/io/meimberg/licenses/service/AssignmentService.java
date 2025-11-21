package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Assignment;
import io.meimberg.licenses.domain.AssignmentStatus;
import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.repository.AssignmentRepository;
import io.meimberg.licenses.repository.ProductVariantRepository;
import io.meimberg.licenses.repository.UserRepository;
import io.meimberg.licenses.web.error.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssignmentService {
  private final AssignmentRepository assignmentRepository;
  private final UserRepository userRepository;
  private final ProductVariantRepository productVariantRepository;

  public AssignmentService(
      AssignmentRepository assignmentRepository,
      UserRepository userRepository,
      ProductVariantRepository productVariantRepository
  ) {
    this.assignmentRepository = assignmentRepository;
    this.userRepository = userRepository;
    this.productVariantRepository = productVariantRepository;
  }

  @Transactional(readOnly = true)
  public Optional<Assignment> get(UUID id) {
    return assignmentRepository.findById(id);
  }

  @Transactional
  public Assignment assign(UUID userId, UUID productVariantId, Instant startsAt, String note) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    ProductVariant variant = productVariantRepository.findById(productVariantId)
        .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + productVariantId));

    // uniqueness: one ACTIVE per (user, variant)
    if (assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, productVariantId, AssignmentStatus.ACTIVE)) {
      throw new ConflictException("Active assignment already exists for user and variant");
    }

    // capacity check
    Integer capacity = variant.getCapacity();
    if (capacity != null) {
      long activeCount = assignmentRepository.countByProductVariantIdAndStatus(productVariantId, AssignmentStatus.ACTIVE);
      if (activeCount >= capacity) {
        throw new ConflictException("Variant capacity exceeded");
      }
    }

    Assignment assignment = new Assignment();
    assignment.setId(UUID.randomUUID());
    assignment.setUser(user);
    assignment.setProductVariant(variant);
    assignment.setStatus(AssignmentStatus.ACTIVE);
    assignment.setStartsAt(startsAt);
    assignment.setNote(note);
    return assignmentRepository.save(assignment);
  }

  @Transactional
  public Assignment revoke(UUID assignmentId, Instant endsAt) {
    Assignment assignment = assignmentRepository.findById(assignmentId)
        .orElseThrow(() -> new EntityNotFoundException("Assignment not found: " + assignmentId));
    if (assignment.getStatus() == AssignmentStatus.REVOKED) {
      return assignment; // idempotent
    }
    assignment.setStatus(AssignmentStatus.REVOKED);
    assignment.setEndsAt(endsAt == null ? Instant.now() : endsAt);
    return assignmentRepository.save(assignment);
  }

  @Transactional(readOnly = true)
  public long activeCount(UUID productVariantId) {
    return assignmentRepository.countByProductVariantIdAndStatus(productVariantId, AssignmentStatus.ACTIVE);
  }
}


