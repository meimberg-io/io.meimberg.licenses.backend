package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Manufacturer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T15:50:40+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class ManufacturerMapperImpl implements ManufacturerMapper {

    @Override
    public io.meimberg.licenses.web.dto.Manufacturer toDto(Manufacturer entity) {
        if ( entity == null ) {
            return null;
        }

        io.meimberg.licenses.web.dto.Manufacturer manufacturer = new io.meimberg.licenses.web.dto.Manufacturer();

        manufacturer.setId( entity.getId() );
        manufacturer.setName( entity.getName() );

        manufacturer.setUrl( toNullableString(entity.getUrl()) );
        manufacturer.setDescription( toNullableString(entity.getDescription()) );

        return manufacturer;
    }
}
