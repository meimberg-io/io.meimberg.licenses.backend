package io.meimberg.licenses.web.mapper;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

  private UserMapper mapper;
  private io.meimberg.licenses.domain.User entity;

  @BeforeEach
  void setUp() {
    mapper = new UserMapperImpl();
    entity = new io.meimberg.licenses.domain.User();
    entity.setId(UUID.randomUUID());
    entity.setEmail("test@example.com");
    entity.setDisplayName("Test User");
    entity.setCreatedAt(Instant.now());
    entity.setUpdatedAt(Instant.now());
  }

  @Test
  void toDto() {
    io.meimberg.licenses.web.dto.User dto = mapper.toDto(entity);

    assertThat(dto).isNotNull();
    assertThat(dto.getId()).isEqualTo(entity.getId());
    assertThat(dto.getEmail()).isEqualTo(entity.getEmail());
    assertThat(dto.getDisplayName()).isEqualTo(entity.getDisplayName());
  }
}

