package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.web.dto.Assignment;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-15T23:15:42+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AssignmentMapperImpl implements AssignmentMapper {

    @Override
    public Assignment toDto(io.meimberg.licenses.domain.Assignment entity) {
        if ( entity == null ) {
            return null;
        }

        Assignment assignment = new Assignment();

        assignment.setUserId( entityUserId( entity ) );
        assignment.setProductVariantId( entityProductVariantId( entity ) );
        assignment.setId( entity.getId() );

        assignment.setNote( toNullableString(entity.getNote()) );

        return assignment;
    }

    private UUID entityUserId(io.meimberg.licenses.domain.Assignment assignment) {
        if ( assignment == null ) {
            return null;
        }
        User user = assignment.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID entityProductVariantId(io.meimberg.licenses.domain.Assignment assignment) {
        if ( assignment == null ) {
            return null;
        }
        ProductVariant productVariant = assignment.getProductVariant();
        if ( productVariant == null ) {
            return null;
        }
        UUID id = productVariant.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
