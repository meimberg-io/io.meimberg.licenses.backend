package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.AssignmentStatus;
import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.web.dto.Assignment;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T01:28:12+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
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
        assignment.setStatus( assignmentStatusToStatusEnum( entity.getStatus() ) );

        assignment.setStartsAt( toNullableTime(entity.getStartsAt()) );
        assignment.setEndsAt( toNullableTime(entity.getEndsAt()) );
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

    protected Assignment.StatusEnum assignmentStatusToStatusEnum(AssignmentStatus assignmentStatus) {
        if ( assignmentStatus == null ) {
            return null;
        }

        Assignment.StatusEnum statusEnum;

        switch ( assignmentStatus ) {
            case ACTIVE: statusEnum = Assignment.StatusEnum.ACTIVE;
            break;
            case REVOKED: statusEnum = Assignment.StatusEnum.REVOKED;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + assignmentStatus );
        }

        return statusEnum;
    }
}
