package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
  io.meimberg.licenses.web.dto.Product toDto(Product entity);
}




