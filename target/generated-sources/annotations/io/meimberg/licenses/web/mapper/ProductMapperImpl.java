package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T01:28:11+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
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

        return product;
    }
}
