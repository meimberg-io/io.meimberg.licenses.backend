package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Department;
import io.meimberg.licenses.web.dto.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-15T23:15:43+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDto(io.meimberg.licenses.domain.User entity) {
        if ( entity == null ) {
            return null;
        }

        User user = new User();

        user.setDepartmentId( entityDepartmentId( entity ) );
        user.setId( entity.getId() );
        user.setEmail( entity.getEmail() );
        user.setDisplayName( entity.getDisplayName() );

        return user;
    }

    private UUID entityDepartmentId(io.meimberg.licenses.domain.User user) {
        if ( user == null ) {
            return null;
        }
        Department department = user.getDepartment();
        if ( department == null ) {
            return null;
        }
        UUID id = department.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
