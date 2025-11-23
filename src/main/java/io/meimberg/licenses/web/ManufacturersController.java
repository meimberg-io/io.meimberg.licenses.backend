package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.Manufacturer;
import io.meimberg.licenses.repository.ManufacturerRepository;
import io.meimberg.licenses.service.ManufacturerService;
import io.meimberg.licenses.web.api.ManufacturersApi;
import io.meimberg.licenses.web.dto.ManufacturerCreateRequest;
import io.meimberg.licenses.web.dto.ManufacturerUpdateRequest;
import io.meimberg.licenses.web.dto.PageManufacturer;
import io.meimberg.licenses.web.mapper.ManufacturerMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ManufacturersController implements ManufacturersApi {
  private final ManufacturerRepository manufacturerRepository;
  private final ManufacturerService manufacturerService;
  private final ManufacturerMapper manufacturerMapper;

  public ManufacturersController(
      ManufacturerRepository manufacturerRepository,
      ManufacturerService manufacturerService,
      ManufacturerMapper manufacturerMapper
  ) {
    this.manufacturerRepository = manufacturerRepository;
    this.manufacturerService = manufacturerService;
    this.manufacturerMapper = manufacturerMapper;
  }

  @Override
  public ResponseEntity<PageManufacturer> manufacturersGet(Integer page, Integer size, String sort) {
    Pageable pageable = buildPageable(page, size, sort);
    Page<Manufacturer> p = manufacturerRepository.findAll(pageable);
    List<io.meimberg.licenses.web.dto.Manufacturer> content = p.getContent().stream()
        .map(manufacturerMapper::toDto)
        .toList();
    PageManufacturer dto = new PageManufacturer();
    dto.setContent(content);
    dto.setTotalElements((int) p.getTotalElements());
    dto.setTotalPages(p.getTotalPages());
    dto.setSize(p.getSize());
    dto.setNumber(p.getNumber());
    return ResponseEntity.ok(dto);
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Manufacturer> manufacturersIdGet(UUID id) {
    Manufacturer entity = manufacturerRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Manufacturer not found"));
    return ResponseEntity.ok(manufacturerMapper.toDto(entity));
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Manufacturer> manufacturersIdPut(UUID id, @Valid ManufacturerUpdateRequest body) {
    Manufacturer updated = manufacturerService.update(
        id,
        body.getName() != null ? body.getName() : null,
        body.getUrl() != null ? body.getUrl().orElse(null) : null,
        body.getDescription() != null ? body.getDescription().orElse(null) : null
    );
    return ResponseEntity.ok(manufacturerMapper.toDto(updated));
  }

  @Override
  public ResponseEntity<Void> manufacturersIdDelete(UUID id) {
    manufacturerService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Manufacturer> manufacturersPost(@Valid ManufacturerCreateRequest body) {
    Manufacturer created = manufacturerService.create(
        body.getName(),
        body.getUrl() != null ? body.getUrl().orElse(null) : null,
        body.getDescription() != null ? body.getDescription().orElse(null) : null
    );
    return ResponseEntity.created(URI.create("/api/v1/manufacturers/" + created.getId()))
        .body(manufacturerMapper.toDto(created));
  }

  private Pageable buildPageable(Integer page, Integer size, String sort) {
    int p = page == null ? 0 : page;
    int s = size == null ? 20 : size;
    if (sort == null || sort.isBlank()) return PageRequest.of(p, s);
    Sort.Direction dir = Sort.Direction.ASC;
    String prop = sort;
    if (sort.contains(",")) {
      String[] parts = sort.split(",", 2);
      prop = parts[0];
      dir = Sort.Direction.fromOptionalString(parts[1]).orElse(Sort.Direction.ASC);
    }
    return PageRequest.of(p, s, Sort.by(dir, prop));
  }
}

