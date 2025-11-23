package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.ProductCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T17:39:51+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public io.meimberg.licenses.web.dto.ProductCategory toDto(ProductCategory entity) {
        if ( entity == null ) {
            return null;
        }

        io.meimberg.licenses.web.dto.ProductCategory productCategory = new io.meimberg.licenses.web.dto.ProductCategory();

        productCategory.setId( entity.getId() );
        productCategory.setName( entity.getName() );

        return productCategory;
    }
}
