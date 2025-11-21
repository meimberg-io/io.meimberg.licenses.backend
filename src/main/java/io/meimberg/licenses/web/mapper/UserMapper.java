package io.meimberg.licenses.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  io.meimberg.licenses.web.dto.User toDto(io.meimberg.licenses.domain.User entity);
}


