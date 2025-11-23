package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Manufacturer;
import io.meimberg.licenses.repository.ManufacturerRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ManufacturerService {
  private final ManufacturerRepository manufacturerRepository;

  public ManufacturerService(ManufacturerRepository manufacturerRepository) {
    this.manufacturerRepository = manufacturerRepository;
  }

  public Optional<Manufacturer> get(UUID id) {
    return manufacturerRepository.findById(id);
  }

  @Transactional
  public Manufacturer create(String name, String url, String description) {
    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setId(UUID.randomUUID());
    manufacturer.setName(name);
    manufacturer.setUrl(url);
    manufacturer.setDescription(description);
    return manufacturerRepository.save(manufacturer);
  }

  @Transactional
  public Manufacturer update(UUID id, String name, String url, String description) {
    Manufacturer manufacturer = manufacturerRepository.findById(id)
        .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Manufacturer not found"));
    if (name != null) manufacturer.setName(name);
    if (url != null) manufacturer.setUrl(url);
    if (description != null) manufacturer.setDescription(description);
    return manufacturerRepository.save(manufacturer);
  }

  @Transactional
  public void delete(UUID id) {
    if (!manufacturerRepository.existsById(id)) {
      throw new jakarta.persistence.EntityNotFoundException("Manufacturer not found");
    }
    manufacturerRepository.deleteById(id);
  }
}

