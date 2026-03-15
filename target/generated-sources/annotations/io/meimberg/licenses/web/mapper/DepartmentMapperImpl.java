package io.meimberg.licenses.web.mapper;

import io.meimberg.licenses.domain.Department;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-15T23:15:42+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
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
