package io.meimberg.licenses.web.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.meimberg.licenses.domain.Product;
import io.meimberg.licenses.domain.ProductVariant;
import java.util.Map;
import java.util.UUID;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductVariantMapper {
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "attributes", expression = "java(toNullableMap(fromJson(entity.getAttributes())))")
  @Mapping(target = "capacity", expression = "java(toNullableInteger(entity.getCapacity()))")
  io.meimberg.licenses.web.dto.ProductVariant toDto(ProductVariant entity);

  default Map<String, Object> fromJson(String json) {
    if (json == null || json.isBlank()) return null;
    try {
      return new ObjectMapper().readValue(json, Map.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid JSON in attributes", e);
    }
  }

  default String toJson(Map<String, Object> map) {
    if (map == null) return null;
    try {
      return new ObjectMapper().writeValueAsString(map);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid attributes map", e);
    }
  }

  default JsonNullable<Integer> toNullableInteger(Integer v) {
    return v == null ? JsonNullable.undefined() : JsonNullable.of(v);
  }

  @Named("toNullableMap")
  default JsonNullable<Map<String, Object>> toNullableMap(Map<String, Object> v) {
    return v == null ? JsonNullable.undefined() : JsonNullable.of(v);
  }

  @AfterMapping
  default void setProduct(@MappingTarget ProductVariant variant, UUID productId) {
    if (productId == null) return;
    Product p = new Product();
    p.setId(productId);
    variant.setProduct(p);
  }
}


