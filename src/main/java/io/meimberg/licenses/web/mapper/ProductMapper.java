package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
  @Mapping(target = "manufacturerId", expression = "java(toNullableUuid(entity.getManufacturer() != null ? entity.getManufacturer().getId() : null))")
  io.meimberg.licenses.web.dto.Product toDto(Product entity);

  default JsonNullable<java.util.UUID> toNullableUuid(java.util.UUID uuid) {
    return uuid == null ? JsonNullable.undefined() : JsonNullable.of(uuid);
  }
}





