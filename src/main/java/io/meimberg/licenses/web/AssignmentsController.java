package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.Assignment;
import io.meimberg.licenses.domain.AssignmentStatus;
import io.meimberg.licenses.repository.AssignmentRepository;
import io.meimberg.licenses.service.AssignmentService;
import io.meimberg.licenses.web.api.AssignmentsApi;
import io.meimberg.licenses.web.dto.AssignmentCreateRequest;
import io.meimberg.licenses.web.dto.AssignmentUpdateRequest;
import io.meimberg.licenses.web.dto.PageAssignment;
import io.meimberg.licenses.web.mapper.AssignmentMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
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
public class AssignmentsController implements AssignmentsApi {
  private final AssignmentRepository assignmentRepository;
  private final AssignmentService assignmentService;
  private final AssignmentMapper assignmentMapper;

  public AssignmentsController(
      AssignmentRepository assignmentRepository,
      AssignmentService assignmentService,
      AssignmentMapper assignmentMapper
  ) {
    this.assignmentRepository = assignmentRepository;
    this.assignmentService = assignmentService;
    this.assignmentMapper = assignmentMapper;
  }

  @Override
  public ResponseEntity<PageAssignment> assignmentsGet(UUID userId, UUID variantId, String status, Integer page, Integer size, String sort) {
    Pageable pageable = buildPageable(page, size, sort);
    Page<Assignment> result;
    AssignmentStatus st = status == null ? null : AssignmentStatus.valueOf(status);
    if (userId != null && variantId != null && st != null) {
      result = assignmentRepository.findByUserIdAndProductVariantIdAndStatus(userId, variantId, st, pageable);
    } else if (userId != null && variantId != null) {
      result = assignmentRepository.findByUserIdAndProductVariantId(userId, variantId, pageable);
    } else if (userId != null && st != null) {
      result = assignmentRepository.findByUserIdAndStatus(userId, st, pageable);
    } else if (variantId != null && st != null) {
      result = assignmentRepository.findByProductVariantIdAndStatus(variantId, st, pageable);
    } else if (userId != null) {
      result = assignmentRepository.findByUserId(userId, pageable);
    } else if (variantId != null) {
      result = assignmentRepository.findByProductVariantId(variantId, pageable);
    } else if (st != null) {
      result = assignmentRepository.findByStatus(st, pageable);
    } else {
      result = assignmentRepository.findAll(pageable);
    }
    List<io.meimberg.licenses.web.dto.Assignment> content = result.getContent().stream()
        .map(assignmentMapper::toDto)
        .toList();
    PageAssignment dto = new PageAssignment();
    dto.setContent(content);
    dto.setTotalElements((int) result.getTotalElements());
    dto.setTotalPages(result.getTotalPages());
    dto.setSize(result.getSize());
    dto.setNumber(result.getNumber());
    return ResponseEntity.ok(dto);
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Assignment> assignmentsIdGet(UUID id) {
    Assignment a = assignmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
    return ResponseEntity.ok(assignmentMapper.toDto(a));
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Assignment> assignmentsIdPatch(UUID id, @Valid AssignmentUpdateRequest body) {
    Assignment updated;
    if (body.getStatus() != null) {
      AssignmentStatus st = AssignmentStatus.valueOf(body.getStatus().getValue());
      if (st == AssignmentStatus.REVOKED) {
        OffsetDateTime endsAt = (body.getEndsAt() != null && body.getEndsAt().isPresent()) ? body.getEndsAt().get() : null;
        updated = assignmentService.revoke(id, endsAt == null ? null : endsAt.toInstant());
      } else {
        updated = assignmentService.reactivate(id);
      }
    } else {
      updated = assignmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Assignment not found"));
    }
    return ResponseEntity.ok(assignmentMapper.toDto(updated));
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Assignment> assignmentsPost(@Valid AssignmentCreateRequest body) {
    OffsetDateTime startsAt = (body.getStartsAt() != null && body.getStartsAt().isPresent()) ? body.getStartsAt().get() : null;
    String note = (body.getNote() != null && body.getNote().isPresent()) ? body.getNote().get() : null;
    Assignment created = assignmentService.assign(body.getUserId(), body.getProductVariantId(), startsAt == null ? null : startsAt.toInstant(), note);
    return ResponseEntity.status(201).body(assignmentMapper.toDto(created));
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


