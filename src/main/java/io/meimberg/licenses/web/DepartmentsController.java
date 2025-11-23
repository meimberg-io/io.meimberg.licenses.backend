package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.Department;
import io.meimberg.licenses.repository.DepartmentRepository;
import io.meimberg.licenses.service.DepartmentService;
import io.meimberg.licenses.web.mapper.DepartmentMapper;
import jakarta.persistence.EntityNotFoundException;
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
public class DepartmentsController implements io.meimberg.licenses.web.api.DepartmentsApi {
  private final DepartmentRepository departmentRepository;
  private final DepartmentService departmentService;
  private final DepartmentMapper departmentMapper;

  public DepartmentsController(
      DepartmentRepository departmentRepository,
      DepartmentService departmentService,
      DepartmentMapper departmentMapper
  ) {
    this.departmentRepository = departmentRepository;
    this.departmentService = departmentService;
    this.departmentMapper = departmentMapper;
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.PageDepartment> departmentsGet(Integer page, Integer size, String sort) {
    Pageable pageable = buildPageable(page, size, sort);
    Page<Department> p = departmentRepository.findAll(pageable);
    List<io.meimberg.licenses.web.dto.Department> content = p.getContent().stream()
        .map(departmentMapper::toDto)
        .toList();
    io.meimberg.licenses.web.dto.PageDepartment dto = new io.meimberg.licenses.web.dto.PageDepartment();
    dto.setContent(content);
    dto.setTotalElements((int) p.getTotalElements());
    dto.setTotalPages(p.getTotalPages());
    dto.setSize(p.getSize());
    dto.setNumber(p.getNumber());
    return ResponseEntity.ok(dto);
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Department> departmentsIdGet(UUID id) {
    Department entity = departmentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Department not found"));
    return ResponseEntity.ok(departmentMapper.toDto(entity));
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Department>
      departmentsIdPut(UUID id, io.meimberg.licenses.web.dto.DepartmentUpdateRequest body) {
    Department updated = departmentService.update(
        id,
        body.getName() != null ? body.getName() : null
    );
    return ResponseEntity.ok(departmentMapper.toDto(updated));
  }

  @Override
  public ResponseEntity<Void> departmentsIdDelete(UUID id) {
    departmentService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<io.meimberg.licenses.web.dto.Department>
      departmentsPost(io.meimberg.licenses.web.dto.DepartmentCreateRequest body) {
    Department created = departmentService.create(body.getName());
    return ResponseEntity.created(URI.create("/api/v1/departments/" + created.getId()))
        .body(departmentMapper.toDto(created));
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

