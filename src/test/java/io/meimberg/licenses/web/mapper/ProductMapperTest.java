package io.meimberg.licenses.web.mapper;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

  private ProductMapper mapper;
  private io.meimberg.licenses.domain.Product entity;

  @BeforeEach
  void setUp() {
    mapper = new ProductMapperImpl();
    entity = new io.meimberg.licenses.domain.Product();
    entity.setId(UUID.randomUUID());
    entity.setKey("test-product");
    entity.setName("Test Product");
    entity.setCreatedAt(Instant.now());
    entity.setUpdatedAt(Instant.now());
  }

  @Test
  void toDto() {
    io.meimberg.licenses.web.dto.Product dto = mapper.toDto(entity);

    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(entity.getId());
    assertThat(dto.getKey()).isEqualTo(entity.getKey());
    assertThat(dto.getName()).isEqualTo(entity.getName());
  }
}

