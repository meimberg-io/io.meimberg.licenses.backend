package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-15T23:15:43+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public io.meimberg.licenses.web.dto.Product toDto(Product entity) {
        if ( entity == null ) {
            return null;
        }

        io.meimberg.licenses.web.dto.Product product = new io.meimberg.licenses.web.dto.Product();

        product.setId( entity.getId() );
        product.setKey( entity.getKey() );
        product.setName( entity.getName() );
        product.setDescription( entity.getDescription() );

        product.setManufacturerId( toNullableUuid(entity.getManufacturer() != null ? entity.getManufacturer().getId() : null) );
        product.setCategoryId( toNullableUuid(entity.getCategory() != null ? entity.getCategory().getId() : null) );

        return product;
    }
}
