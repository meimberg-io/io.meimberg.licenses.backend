package io.meimberg.licenses.web.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
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
    entity.setCapacity(10);
    entity.setCreatedAt(Instant.now());
    entity.setUpdatedAt(Instant.now());
  }

  @Test
  void toDto_withCapacity() {
    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(entity.getId());
    assertThat(dto.getProductId()).isEqualTo(product.getId());
    assertThat(dto.getKey()).isEqualTo(entity.getKey());
    assertThat(dto.getName()).isEqualTo(entity.getName());
    assertThat(dto.getCapacity()).isNotNull();
    assertThat(dto.getCapacity().isPresent()).isTrue();
    assertThat(dto.getCapacity().get()).isEqualTo(10);
  }

  @Test
  void toDto_withoutCapacity() {
    entity.setCapacity(null);

    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto.getCapacity()).isNotNull();
    assertThat(dto.getCapacity().isPresent()).isFalse();
  }

  @Test
  void toDto_withAttributes() throws Exception {
    Map<String, Object> attrs = Map.of("key1", "value1", "key2", 42);
    String json = new ObjectMapper().writeValueAsString(attrs);
    entity.setAttributes(json);

    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto.getAttributes()).isNotNull();
    assertThat(dto.getAttributes().isPresent()).isTrue();
    assertThat(dto.getAttributes().get()).containsEntry("key1", "value1");
    assertThat(dto.getAttributes().get()).containsEntry("key2", 42);
  }

  @Test
  void toDto_withoutAttributes() {
    entity.setAttributes(null);

    io.meimberg.licenses.web.dto.ProductVariant dto = mapper.toDto(entity);

    assertThat(dto.getAttributes()).isNotNull();
    assertThat(dto.getAttributes().isPresent()).isFalse();
  }

  @Test
  void toNullableInteger() {
    JsonNullable<Integer> result1 = mapper.toNullableInteger(42);
    assertThat(result1.isPresent()).isTrue();
    assertThat(result1.get()).isEqualTo(42);

    JsonNullable<Integer> result2 = mapper.toNullableInteger(null);
    assertThat(result2.isPresent()).isFalse();
  }
}

