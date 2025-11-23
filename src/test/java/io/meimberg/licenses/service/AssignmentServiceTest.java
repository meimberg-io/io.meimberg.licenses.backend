package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Assignment;
import io.meimberg.licenses.domain.Product;
import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.repository.AssignmentRepository;
import io.meimberg.licenses.repository.ProductVariantRepository;
import io.meimberg.licenses.repository.UserRepository;
import io.meimberg.licenses.web.error.ConflictException;
import jakarta.persistence.EntityNotFoundException;
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

    user = new User();
    user.setId(userId);
    user.setEmail("test@example.com");
    user.setDisplayName("Test User");
  }

  @Test
  void assign_success() {
    String note = "Test note";

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
    when(assignmentRepository.existsByUserIdAndProductVariantId(userId, variantId))
        .thenReturn(false);
    when(assignmentRepository.save(any(Assignment.class))).thenAnswer(invocation -> {
      Assignment a = invocation.getArgument(0);
      a.setId(assignmentId);
      return a;
    });

    Assignment result = assignmentService.assign(userId, variantId, note);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(assignmentId);
    assertThat(result.getUser()).isEqualTo(user);
    assertThat(result.getProductVariant()).isEqualTo(variant);
    assertThat(result.getNote()).isEqualTo(note);

    verify(assignmentRepository).save(any(Assignment.class));
  }

  @Test
  void assign_userNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> assignmentService.assign(userId, variantId, null))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("User not found");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void assign_variantNotFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> assignmentService.assign(userId, variantId, null))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Variant not found");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }

  @Test
  void assign_duplicateAssignment() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));
    when(assignmentRepository.existsByUserIdAndProductVariantId(userId, variantId))
        .thenReturn(true);

    assertThatThrownBy(() -> assignmentService.assign(userId, variantId, null))
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("Assignment already exists");

    verify(assignmentRepository, never()).save(any(Assignment.class));
  }


  @Test
  void delete_success() {
    when(assignmentRepository.existsById(assignmentId)).thenReturn(true);

    assignmentService.delete(assignmentId);

    verify(assignmentRepository).deleteById(assignmentId);
  }

  @Test
  void delete_assignmentNotFound() {
    when(assignmentRepository.existsById(assignmentId)).thenReturn(false);

    assertThatThrownBy(() -> assignmentService.delete(assignmentId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("Assignment not found");

    verify(assignmentRepository, never()).deleteById(any(UUID.class));
  }


  @Test
  void count() {
    when(assignmentRepository.countByProductVariantId(variantId))
        .thenReturn(7L);

    long count = assignmentService.count(variantId);

    assertThat(count).isEqualTo(7L);
    verify(assignmentRepository).countByProductVariantId(variantId);
  }
}




