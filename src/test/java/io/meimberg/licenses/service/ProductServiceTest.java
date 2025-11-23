package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Product;
import io.meimberg.licenses.repository.ProductRepository;
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
class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @InjectMocks
  private ProductService productService;

  private UUID productId;
  private Product product;

  @BeforeEach
  void setUp() {
    productId = UUID.randomUUID();
    product = new Product();
    product.setId(productId);
    product.setKey("test-product");
    product.setName("Test Product");
  }

  @Test
  void get_success() {
    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    Optional<Product> result = productService.get(productId);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(product);
    verify(productRepository).findById(productId);
  }

  @Test
  void get_notFound() {
    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    Optional<Product> result = productService.get(productId);

    assertThat(result).isEmpty();
  }
}



