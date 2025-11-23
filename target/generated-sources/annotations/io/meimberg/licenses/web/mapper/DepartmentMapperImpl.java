package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Department;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-23T17:39:51+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public io.meimberg.licenses.web.dto.Department toDto(Department entity) {
        if ( entity == null ) {
            return null;
        }

        io.meimberg.licenses.web.dto.Department department = new io.meimberg.licenses.web.dto.Department();

        department.setId( entity.getId() );
        department.setName( entity.getName() );

        return department;
    }
}
