package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.ProductCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-15T23:15:42+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
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
