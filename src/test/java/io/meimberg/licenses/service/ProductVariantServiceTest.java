package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Product;
import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.repository.ProductVariantRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductVariantServiceTest {

  @Mock
  private ProductVariantRepository productVariantRepository;

  @InjectMocks
  private ProductVariantService productVariantService;

  private UUID variantId;
  private UUID productId;
  private Product product;
  private ProductVariant variant;

  @BeforeEach
  void setUp() {
    variantId = UUID.randomUUID();
    productId = UUID.randomUUID();

    product = new Product();
    product.setId(productId);
    product.setKey("test-product");
    product.setName("Test Product");

    variant = new ProductVariant();
    variant.setId(variantId);
    variant.setProduct(product);
    variant.setKey("variant-key");
    variant.setName("Variant Name");
    variant.setCapacity(10);
  }

  @Test
  void get_success() {
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.of(variant));

    Optional<ProductVariant> result = productVariantService.get(variantId);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(variant);
    verify(productVariantRepository).findById(variantId);
  }

  @Test
  void get_notFound() {
    when(productVariantRepository.findById(variantId)).thenReturn(Optional.empty());

    Optional<ProductVariant> result = productVariantService.get(variantId);

    assertThat(result).isEmpty();
  }

  @Test
  void listByProduct() {
    ProductVariant variant2 = new ProductVariant();
    variant2.setId(UUID.randomUUID());
    variant2.setProduct(product);
    variant2.setKey("variant-key-2");
    variant2.setName("Variant 2");

    when(productVariantRepository.findByProductId(productId)).thenReturn(List.of(variant, variant2));

    List<ProductVariant> result = productVariantService.listByProduct(productId);

    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(variant, variant2);
    verify(productVariantRepository).findByProductId(productId);
  }

  @Test
  void listByProduct_empty() {
    when(productVariantRepository.findByProductId(productId)).thenReturn(List.of());

    List<ProductVariant> result = productVariantService.listByProduct(productId);

    assertThat(result).isEmpty();
  }
}


