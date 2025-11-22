package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.AssignmentStatus;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import static org.assertj.core.api.Assertions.assertThat;

class AssignmentMapperTest {

  private AssignmentMapper mapper;
  private io.meimberg.licenses.domain.Assignment entity;
  private io.meimberg.licenses.domain.User user;
  private io.meimberg.licenses.domain.ProductVariant variant;

  @BeforeEach
  void setUp() {
    mapper = new AssignmentMapperImpl();

    user = new io.meimberg.licenses.domain.User();
    user.setId(UUID.randomUUID());
    user.setEmail("test@example.com");
    user.setDisplayName("Test User");

    io.meimberg.licenses.domain.Product product = new io.meimberg.licenses.domain.Product();
    product.setId(UUID.randomUUID());
    product.setKey("test-product");
    product.setName("Test Product");

    variant = new io.meimberg.licenses.domain.ProductVariant();
    variant.setId(UUID.randomUUID());
    variant.setProduct(product);
    variant.setKey("variant-key");
    variant.setName("Variant Name");

    entity = new io.meimberg.licenses.domain.Assignment();
    entity.setId(UUID.randomUUID());
    entity.setUser(user);
    entity.setProductVariant(variant);
    entity.setStatus(AssignmentStatus.ACTIVE);
    entity.setStartsAt(Instant.now());
    entity.setEndsAt(null);
    entity.setNote("Test note");
  }

  @Test
  void toDto() {
    io.meimberg.licenses.web.dto.Assignment dto = mapper.toDto(entity);

    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(entity.getId());
    assertThat(dto.getUserId()).isEqualTo(user.getId());
    assertThat(dto.getProductVariantId()).isEqualTo(variant.getId());
    assertThat(dto.getStatus()).isEqualTo(io.meimberg.licenses.web.dto.Assignment.StatusEnum.ACTIVE);
    assertThat(dto.getStartsAt()).isNotNull();
    assertThat(dto.getStartsAt().isPresent()).isTrue();
    assertThat(dto.getNote()).isNotNull();
    assertThat(dto.getNote().isPresent()).isTrue();
    assertThat(dto.getNote().get()).isEqualTo("Test note");
  }

  @Test
  void toDto_withNullFields() {
    entity.setStartsAt(null);
    entity.setEndsAt(null);
    entity.setNote(null);

    io.meimberg.licenses.web.dto.Assignment dto = mapper.toDto(entity);

    assertThat(dto.getStartsAt()).isNotNull();
    assertThat(dto.getStartsAt().isPresent()).isFalse();
    assertThat(dto.getEndsAt()).isNotNull();
    assertThat(dto.getEndsAt().isPresent()).isFalse();
    assertThat(dto.getNote()).isNotNull();
    assertThat(dto.getNote().isPresent()).isFalse();
  }

  @Test
  void toNullableTime() {
    Instant instant = Instant.now();
    JsonNullable<java.time.OffsetDateTime> result = mapper.toNullableTime(instant);

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().toInstant()).isEqualTo(instant);

    JsonNullable<java.time.OffsetDateTime> nullResult = mapper.toNullableTime(null);
    assertThat(nullResult.isPresent()).isFalse();
  }

  @Test
  void toNullableString() {
    JsonNullable<String> result1 = mapper.toNullableString("test");
    assertThat(result1.isPresent()).isTrue();
    assertThat(result1.get()).isEqualTo("test");

    JsonNullable<String> result2 = mapper.toNullableString(null);
    assertThat(result2.isPresent()).isFalse();
  }
}

