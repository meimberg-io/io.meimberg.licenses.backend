package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Manufacturer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ManufacturerMapper {
  @Mapping(target = "url", expression = "java(toNullableString(entity.getUrl()))")
  @Mapping(target = "description", expression = "java(toNullableString(entity.getDescription()))")
  io.meimberg.licenses.web.dto.Manufacturer toDto(Manufacturer entity);

  default JsonNullable<String> toNullableString(String s) {
    return s == null ? JsonNullable.undefined() : JsonNullable.of(s);
  }
}

