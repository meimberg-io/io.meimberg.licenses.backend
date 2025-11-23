package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.repository.AssignmentRepository;
import io.meimberg.licenses.repository.ProductVariantRepository;
import io.meimberg.licenses.service.AssignmentService;
import io.meimberg.licenses.web.api.VariantsApi;
import io.meimberg.licenses.web.dto.ProductVariantUpdateRequest;
import io.meimberg.licenses.web.dto.VariantAvailability;
import io.meimberg.licenses.web.mapper.ProductVariantMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class VariantsController implements VariantsApi {
  private final ProductVariantRepository productVariantRepository;
  private final AssignmentService assignmentService;
  private final ProductVariantMapper productVariantMapper;

  public VariantsController(
      ProductVariantRepository productVariantRepository,
      AssignmentService assignmentService,
      ProductVariantMapper productVariantMapper
  ) {
    this.productVariantRepository = productVariantRepository;
    this.assignmentService = assignmentService;
    this.productVariantMapper = productVariantMapper;
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.ProductVariant> variantsIdGet(UUID id) {
    ProductVariant v = productVariantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Variant not found"));
    return ResponseEntity.ok(productVariantMapper.toDto(v));
  }

  @Override
  public ResponseEntity<Void> variantsIdDelete(UUID id) {
    if (!productVariantRepository.existsById(id)) throw new EntityNotFoundException("Variant not found");
    productVariantRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.ProductVariant> variantsIdPut(UUID id, @Valid ProductVariantUpdateRequest body) {
    ProductVariant v = productVariantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Variant not found"));
    if (body.getKey() != null) v.setKey(body.getKey());
    if (body.getName() != null) v.setName(body.getName());
    if (body.getDescription() != null && body.getDescription().isPresent()) {
      v.setDescription(body.getDescription().get());
    } else if (body.getDescription() != null && !body.getDescription().isPresent()) {
      v.setDescription(null);
    }
    if (body.getPrice() != null && body.getPrice().isPresent()) {
      v.setPrice(java.math.BigDecimal.valueOf(body.getPrice().get()));
    } else if (body.getPrice() != null && !body.getPrice().isPresent()) {
      v.setPrice(null);
    }
    v = productVariantRepository.save(v);
    return ResponseEntity.ok(productVariantMapper.toDto(v));
  }

  @Override
  public ResponseEntity<VariantAvailability> variantsIdAvailabilityGet(UUID id) {
    ProductVariant v = productVariantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Variant not found"));
    long count = assignmentService.count(id);
    VariantAvailability dto = new VariantAvailability();
    dto.setCapacity(JsonNullable.of(null));
    dto.setAssignedActiveCount((int) count);
    dto.setAvailable(true);
    return ResponseEntity.ok(dto);
  }
}


