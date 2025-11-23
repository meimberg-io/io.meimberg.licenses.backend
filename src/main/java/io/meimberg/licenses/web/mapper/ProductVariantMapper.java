package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;
import java.math.BigDecimal;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductVariantMapper {
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "description", expression = "java(toNullableString(entity.getDescription()))")
  @Mapping(target = "price", expression = "java(toNullableDouble(entity.getPrice()))")
  io.meimberg.licenses.web.dto.ProductVariant toDto(ProductVariant entity);

  default JsonNullable<String> toNullableString(String s) {
    return s == null ? JsonNullable.undefined() : JsonNullable.of(s);
  }

  default JsonNullable<Double> toNullableDouble(BigDecimal bd) {
    return bd == null ? JsonNullable.undefined() : JsonNullable.of(bd.doubleValue());
  }
}


