package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Manufacturer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-15T23:15:43+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
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
