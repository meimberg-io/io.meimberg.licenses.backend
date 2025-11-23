package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.web.dto.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T15:50:40+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDto(io.meimberg.licenses.domain.User entity) {
        if ( entity == null ) {
            return null;
        }

        User user = new User();

        user.setId( entity.getId() );
        user.setEmail( entity.getEmail() );
        user.setDisplayName( entity.getDisplayName() );

        return user;
    }
}
