package io.meimberg.licenses.web.mapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import static org.assertj.core.api.Assertions.assertThat;

class ProductVariantMapperTest {

  private ProductVariantMapper mapper;
  private io.meimberg.licenses.domain.ProductVariant entity;
  private io.meimberg.licenses.domain.Product product;

  @BeforeEach
  void setUp() {
    mapper = new ProductVariantMapperImpl();
    product = new io.meimberg.licenses.domain.Product();
    product.setId(UUID.randomUUID());
    product.setKey("test-product");
    product.setName("Test Product");

    entity = new io.meimberg.licenses.domain.ProductVariant();
    entity.setId(UUID.randomUUID());
    entity.setProduct(product);
    entity.setKey("variant-key");
    entity.setName("Variant Name");
    entity.setDescription("Test description");
    entity.setCreatedAt(Instant.now());
    entity.setUpdatedAt(Instant.now());
  }

  @Test
  void toDto_withDescription() {
    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(entity.getId());
    assertThat(dto.getProductId()).isEqualTo(product.getId());
    assertThat(dto.getKey()).isEqualTo(entity.getKey());
    assertThat(dto.getName()).isEqualTo(entity.getName());
    assertThat(dto.getDescription()).isNotNull();
    assertThat(dto.getDescription().isPresent()).isTrue();
    assertThat(dto.getDescription().get()).isEqualTo("Test description");
  }

  @Test
  void toDto_withoutDescription() {
    entity.setDescription(null);

    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto.getDescription()).isNotNull();
    assertThat(dto.getDescription().isPresent()).isFalse();
  }

  @Test
  void toDto_withPrice() {
    entity.setPrice(new BigDecimal("99.99"));

    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto.getPrice()).isNotNull();
    assertThat(dto.getPrice().isPresent()).isTrue();
    assertThat(dto.getPrice().get()).isEqualTo(99.99);
  }

  @Test
  void toDto_withoutPrice() {
    entity.setPrice(null);

    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto.getPrice()).isNotNull();
    assertThat(dto.getPrice().isPresent()).isFalse();
  }
}

