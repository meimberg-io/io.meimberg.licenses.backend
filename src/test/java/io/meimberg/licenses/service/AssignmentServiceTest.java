package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Assignment;
import io.meimberg.licenses.domain.AssignmentStatus;
import io.meimberg.licenses.domain.Product;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

  @Mock
  private AssignmentRepository assignmentRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ProductVariantRepository productVariantRepository;

  @InjectMocks
  private AssignmentService assignmentService;

  private UUID userId;
  private UUID variantId;
  private UUID assignmentId;
  private User user;
  private ProductVariant variant;
  private Product product;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    variantId = UUID.randomUUID();
    assignmentId = UUID.randomUUID();

    product = new Product();
    product.setId(UUID.randomUUID());
    product.setKey("test-product");
    product.setName("Test Product");

    variant = new ProductVariant();
    variant.setId(variantId);
    variant.setProduct(product);
    variant.setKey("variant-key");
    variant.setName("Variant Name");
    variant.setCapacity(10);

    user = new User();
    user.setId(userId);
    user.setEmail("test@example.com");
    user.setDisplayName("Test User");
  }

  @Test
  void assign_success() {
    Instant startsAt = Instant.now();
    String note = "Test note";

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
    when(assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, variantId, AssignmentStatus.ACTIVE))
        .thenReturn(false);
    when(assignmentRepository.countByProductVariantIdAndStatus(variantId, AssignmentStatus.ACTIVE))
        .thenReturn(5L);
    when(assignmentRepository.save(any(Assignment.class))).thenAnswer(invocation -> {
      Assignment a = invocation.getArgument(0);
      a.setId(assignmentId);
      return a;
    });

    Assignment result = assignmentService.assign(userId, variantId, startsAt, note);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(assignmentId);
    assertThat(result.getUser()).isEqualTo(user);
    assertThat(result.getProductVariant()).isEqualTo(variant);
    assertThat(result.getStatus()).isEqualTo(AssignmentStatus.ACTIVE);
    assertThat(result.getStartsAt()).isEqualTo(startsAt);
    assertThat(result.getNote()).isEqualTo(note);

    verify(assignmentRepository).save(any(Assignment.class));
  }

  @Test
  void assign_userNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> assignmentService.assign(userId, variantId, Instant.now(), null))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("User not found");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void assign_variantNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> assignmentService.assign(userId, variantId, Instant.now(), null))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Variant not found");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void assign_duplicateActiveAssignment() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
    when(assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, variantId, AssignmentStatus.ACTIVE))
        .thenReturn(true);

    assertThatThrownBy(() -> assignmentService.assign(userId, variantId, Instant.now(), null))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("Active assignment already exists");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void assign_capacityExceeded() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
    when(assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, variantId, AssignmentStatus.ACTIVE))
        .thenReturn(false);
    when(assignmentRepository.countByProductVariantIdAndStatus(variantId, AssignmentStatus.ACTIVE))
        .thenReturn(10L);

    assertThatThrownBy(() -> assignmentService.assign(userId, variantId, Instant.now(), null))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("Variant capacity exceeded");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void assign_noCapacityLimit() {
    variant.setCapacity(null);
    Instant startsAt = Instant.now();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
    when(assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, variantId, AssignmentStatus.ACTIVE))
        .thenReturn(false);
    when(assignmentRepository.save(any(Assignment.class))).thenAnswer(invocation -> {
      Assignment a = invocation.getArgument(0);
      a.setId(assignmentId);
      return a;
    });

    Assignment result = assignmentService.assign(userId, variantId, startsAt, null);

    assertThat(result).isNotNull();
    assertThat(result.getStatus()).isEqualTo(AssignmentStatus.ACTIVE);
    verify(assignmentRepository, never()).countByProductVariantIdAndStatus(any(), any());
    verify(assignmentRepository).save(any(Assignment.class));
  }

  @Test
  void revoke_success() {
    Instant endsAt = Instant.now();
    Assignment assignment = new Assignment();
    assignment.setId(assignmentId);
    assignment.setUser(user);
    assignment.setProductVariant(variant);
    assignment.setStatus(AssignmentStatus.ACTIVE);

    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
    when(assignmentRepository.save(assignment)).thenReturn(assignment);

    Assignment result = assignmentService.revoke(assignmentId, endsAt);

    assertThat(result.getStatus()).isEqualTo(AssignmentStatus.REVOKED);
    assertThat(result.getEndsAt()).isEqualTo(endsAt);
    verify(assignmentRepository).save(assignment);
  }

  @Test
  void revoke_withNullEndsAt() {
    Assignment assignment = new Assignment();
    assignment.setId(assignmentId);
    assignment.setStatus(AssignmentStatus.ACTIVE);

    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
    when(assignmentRepository.save(assignment)).thenReturn(assignment);

    Assignment result = assignmentService.revoke(assignmentId, null);

    assertThat(result.getStatus()).isEqualTo(AssignmentStatus.REVOKED);
    assertThat(result.getEndsAt()).isNotNull();
    verify(assignmentRepository).save(assignment);
  }

  @Test
  void revoke_idempotent() {
    Assignment assignment = new Assignment();
    assignment.setId(assignmentId);
    assignment.setStatus(AssignmentStatus.REVOKED);
    assignment.setEndsAt(Instant.now());

    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

    Assignment result = assignmentService.revoke(assignmentId, Instant.now());

    assertThat(result).isSameAs(assignment);
    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void revoke_assignmentNotFound() {
    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> assignmentService.revoke(assignmentId, Instant.now()))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Assignment not found");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void reactivate_success() {
    Assignment assignment = new Assignment();
    assignment.setId(assignmentId);
    assignment.setUser(user);
    assignment.setProductVariant(variant);
    assignment.setStatus(AssignmentStatus.REVOKED);
    assignment.setEndsAt(Instant.now());

    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
    when(assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, variantId, AssignmentStatus.ACTIVE))
        .thenReturn(false);
    when(assignmentRepository.countByProductVariantIdAndStatus(variantId, AssignmentStatus.ACTIVE))
        .thenReturn(5L);
    when(assignmentRepository.save(assignment)).thenReturn(assignment);

    Assignment result = assignmentService.reactivate(assignmentId);

    assertThat(result.getStatus()).isEqualTo(AssignmentStatus.ACTIVE);
    assertThat(result.getEndsAt()).isNull();
    verify(assignmentRepository).save(assignment);
  }

  @Test
  void reactivate_assignmentNotFound() {
    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> assignmentService.reactivate(assignmentId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Assignment not found");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void reactivate_duplicateActiveExists() {
    Assignment assignment = new Assignment();
    assignment.setId(assignmentId);
    assignment.setUser(user);
    assignment.setProductVariant(variant);
    assignment.setStatus(AssignmentStatus.REVOKED);

    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
    when(assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, variantId, AssignmentStatus.ACTIVE))
        .thenReturn(true);

    assertThatThrownBy(() -> assignmentService.reactivate(assignmentId))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("Active assignment already exists");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void reactivate_capacityExceeded() {
    Assignment assignment = new Assignment();
    assignment.setId(assignmentId);
    assignment.setUser(user);
    assignment.setProductVariant(variant);
    assignment.setStatus(AssignmentStatus.REVOKED);

    when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
    when(assignmentRepository.existsByUserIdAndProductVariantIdAndStatus(userId, variantId, AssignmentStatus.ACTIVE))
        .thenReturn(false);
    when(assignmentRepository.countByProductVariantIdAndStatus(variantId, AssignmentStatus.ACTIVE))
        .thenReturn(10L);

    assertThatThrownBy(() -> assignmentService.reactivate(assignmentId))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("Variant capacity exceeded");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void activeCount() {
    when(assignmentRepository.countByProductVariantIdAndStatus(variantId, AssignmentStatus.ACTIVE))
        .thenReturn(7L);

    long count = assignmentService.activeCount(variantId);

    assertThat(count).isEqualTo(7L);
    verify(assignmentRepository).countByProductVariantIdAndStatus(variantId, AssignmentStatus.ACTIVE);
  }
}


