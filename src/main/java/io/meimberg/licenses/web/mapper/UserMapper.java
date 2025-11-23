package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  @Mapping(target = "departmentId", source = "department.id")
  io.meimberg.licenses.web.dto.User toDto(User entity);
}


