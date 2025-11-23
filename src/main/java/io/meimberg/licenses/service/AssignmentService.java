package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Assignment;
import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.repository.AssignmentRepository;
import io.meimberg.licenses.repository.ProductVariantRepository;
import io.meimberg.licenses.repository.UserRepository;
import io.meimberg.licenses.web.error.ConflictException;
import jakarta.persistence.EntityNotFoundException;
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
  public Assignment assign(UUID userId, UUID productVariantId, String note) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    ProductVariant variant = productVariantRepository.findById(productVariantId)
        .orElseThrow(() -> new EntityNotFoundException("Variant not found: " + productVariantId));

    // uniqueness: one assignment per (user, variant)
    if (assignmentRepository.existsByUserIdAndProductVariantId(userId, productVariantId)) {
      throw new ConflictException("Assignment already exists for user and variant");
    }

    Assignment assignment = new Assignment();
    assignment.setId(UUID.randomUUID());
    assignment.setUser(user);
    assignment.setProductVariant(variant);
    assignment.setNote(note);
    return assignmentRepository.save(assignment);
  }

  @Transactional
  public void delete(UUID assignmentId) {
    if (!assignmentRepository.existsById(assignmentId)) {
      throw new EntityNotFoundException("Assignment not found: " + assignmentId);
    }
    assignmentRepository.deleteById(assignmentId);
  }

  @Transactional(readOnly = true)
  public long count(UUID productVariantId) {
    return assignmentRepository.countByProductVariantId(productVariantId);
  }
}


