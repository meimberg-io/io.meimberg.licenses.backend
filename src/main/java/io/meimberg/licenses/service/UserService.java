package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public Optional<User> get(UUID id) {
    return userRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public List<User> list() {
    return userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Page<User> search(String email, Pageable pageable) {
    if (email == null || email.isBlank()) {
      return userRepository.findAll(pageable);
    }
    return userRepository.findByEmailContainingIgnoreCase(email, pageable);
  }

  @Transactional
  public User create(String email, String displayName) {
    User user = new User();
    user.setId(UUID.randomUUID());
    user.setEmail(email);
    user.setDisplayName(displayName);
    return userRepository.save(user);
  }

  @Transactional
  public User update(UUID id, String email, String displayName) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    if (email != null) user.setEmail(email);
    if (displayName != null) user.setDisplayName(displayName);
    return userRepository.save(user);
  }

  @Transactional
  public void delete(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException("User not found: " + id);
    }
    userRepository.deleteById(id);
  }
}


