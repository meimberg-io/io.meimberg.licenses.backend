package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductCategoryMapper {
  io.meimberg.licenses.web.dto.ProductCategory toDto(ProductCategory entity);
}

