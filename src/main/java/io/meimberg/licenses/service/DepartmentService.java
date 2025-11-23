package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.Department;
import io.meimberg.licenses.repository.DepartmentRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DepartmentService {
  private final DepartmentRepository departmentRepository;

  public DepartmentService(DepartmentRepository departmentRepository) {
    this.departmentRepository = departmentRepository;
  }

  public Optional<Department> get(UUID id) {
    return departmentRepository.findById(id);
  }

  @Transactional
  public Department create(String name) {
    Department department = new Department();
    department.setId(UUID.randomUUID());
    department.setName(name);
    return departmentRepository.save(department);
  }

  @Transactional
  public Department update(UUID id, String name) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Department not found"));
    if (name != null) department.setName(name);
    return departmentRepository.save(department);
  }

  @Transactional
  public void delete(UUID id) {
    if (!departmentRepository.existsById(id)) {
      throw new jakarta.persistence.EntityNotFoundException("Department not found");
    }
    departmentRepository.deleteById(id);
  }
}

