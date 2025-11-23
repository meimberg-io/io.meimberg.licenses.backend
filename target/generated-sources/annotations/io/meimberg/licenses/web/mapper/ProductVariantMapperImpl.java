package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Product;
import io.meimberg.licenses.web.dto.ProductVariant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T01:28:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class ProductVariantMapperImpl implements ProductVariantMapper {

    @Override
    public ProductVariant toDto(io.meimberg.licenses.domain.ProductVariant entity) {
        if ( entity == null ) {
            return null;
        }

        ProductVariant productVariant = new ProductVariant();

        productVariant.setProductId( entityProductId( entity ) );
        productVariant.setId( entity.getId() );
        productVariant.setKey( entity.getKey() );
        productVariant.setName( entity.getName() );

        productVariant.setAttributes( toNullableMap(fromJson(entity.getAttributes())) );
        productVariant.setCapacity( toNullableInteger(entity.getCapacity()) );

        return productVariant;
    }

    private UUID entityProductId(io.meimberg.licenses.domain.ProductVariant productVariant) {
        if ( productVariant == null ) {
            return null;
        }
        Product product = productVariant.getProduct();
        if ( product == null ) {
            return null;
        }
        UUID id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
