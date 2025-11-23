package io.meimberg.licenses.repository;

import io.meimberg.licenses.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);
  Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
  Page<User> findByDepartmentId(UUID departmentId, Pageable pageable);
  Page<User> findByEmailContainingIgnoreCaseAndDepartmentId(String email, UUID departmentId, Pageable pageable);
  List<User> findByDepartmentId(UUID departmentId);
}


