package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.Manufacturer;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, UUID> {
  Optional<Manufacturer> findByName(String name);
}

