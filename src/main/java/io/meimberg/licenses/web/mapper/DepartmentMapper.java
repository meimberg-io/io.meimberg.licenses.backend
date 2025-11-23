package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {
  io.meimberg.licenses.web.dto.Department toDto(Department entity);
}

