package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.AssignmentStatus;
import io.meimberg.licenses.domain.ProductVariant;
import io.meimberg.licenses.repository.AssignmentRepository;
import io.meimberg.licenses.repository.ProductVariantRepository;
import io.meimberg.licenses.web.api.VariantsApi;
import io.meimberg.licenses.web.dto.ProductVariantUpdateRequest;
import io.meimberg.licenses.web.dto.VariantAvailability;
import io.meimberg.licenses.web.mapper.ProductVariantMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VariantsController implements VariantsApi {
  private final ProductVariantRepository productVariantRepository;
  private final AssignmentRepository assignmentRepository;
  private final ProductVariantMapper productVariantMapper;

  public VariantsController(
      ProductVariantRepository productVariantRepository,
      AssignmentRepository assignmentRepository,
      ProductVariantMapper productVariantMapper
  ) {
    this.productVariantRepository = productVariantRepository;
    this.assignmentRepository = assignmentRepository;
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
    if (body.getCapacity() != null && body.getCapacity().isPresent()) v.setCapacity(body.getCapacity().get());
    if (body.getAttributes() != null && body.getAttributes().isPresent()) v.setAttributes(productVariantMapper.toJson(body.getAttributes().get()));
    v = productVariantRepository.save(v);
    return ResponseEntity.ok(productVariantMapper.toDto(v));
  }

  @Override
  public ResponseEntity<VariantAvailability> variantsIdAvailabilityGet(UUID id) {
    ProductVariant v = productVariantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Variant not found"));
    long active = assignmentRepository.countByProductVariantIdAndStatus(id, AssignmentStatus.ACTIVE);
    VariantAvailability dto = new VariantAvailability();
    dto.setCapacity(JsonNullable.of(v.getCapacity()));
    dto.setAssignedActiveCount((int) active);
    dto.setAvailable(v.getCapacity() == null || active < v.getCapacity());
    return ResponseEntity.ok(dto);
  }
}


