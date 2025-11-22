package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Assignment;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AssignmentMapper {
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "productVariantId", source = "productVariant.id")
  @Mapping(target = "startsAt", expression = "java(toNullableTime(entity.getStartsAt()))")
  @Mapping(target = "endsAt", expression = "java(toNullableTime(entity.getEndsAt()))")
  @Mapping(target = "note", expression = "java(toNullableString(entity.getNote()))")
  io.meimberg.licenses.web.dto.Assignment toDto(Assignment entity);

  default JsonNullable<OffsetDateTime> toNullableTime(java.time.Instant instant) {
    if (instant == null) return JsonNullable.undefined();
    return JsonNullable.of(OffsetDateTime.ofInstant(instant, ZoneOffset.UTC));
    }

  default JsonNullable<String> toNullableString(String s) {
    return s == null ? JsonNullable.undefined() : JsonNullable.of(s);
  }
}


