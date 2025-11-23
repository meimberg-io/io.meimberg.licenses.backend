package io.meimberg.licenses.web;

import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.service.UserService;
import io.meimberg.licenses.web.dto.PageUser;
import io.meimberg.licenses.web.dto.UserCreateRequest;
import io.meimberg.licenses.web.dto.UserUpdateRequest;
import io.meimberg.licenses.web.mapper.UserMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1")
public class UsersController {
  private final UserService userService;
  private final UserMapper userMapper;

  public UsersController(UserService userService, UserMapper userMapper) {
    this.userService = userService;
    this.userMapper = userMapper;
  }

  @GetMapping("/users")
  public ResponseEntity<PageUser> listUsers(
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "departmentId", required = false) UUID departmentId,
      @PageableDefault Pageable pageable
  ) {
    Page<User> page = userService.search(email, departmentId, pageable);
    List<io.meimberg.licenses.web.dto.User> content = page.getContent().stream()
        .map(userMapper::toDto)
        .toList();
    PageUser dto = new PageUser();
    dto.setContent(content);
    dto.setTotalElements((int) page.getTotalElements());
    dto.setTotalPages(page.getTotalPages());
    dto.setSize(page.getSize());
    dto.setNumber(page.getNumber());
    return ResponseEntity.ok(dto);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<io.meimberg.licenses.web.dto.User> getUser(@PathVariable("id") UUID id) {
    return userService.get(id)
        .map(userMapper::toDto)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/users")
  public ResponseEntity<io.meimberg.licenses.web.dto.User> createUser(
      @Valid @RequestBody UserCreateRequest request
  ) {
    User created = userService.create(request.getEmail(), request.getDisplayName(), request.getDepartmentId());
    io.meimberg.licenses.web.dto.User body = userMapper.toDto(created);
    return ResponseEntity.created(URI.create("/api/v1/users/" + created.getId())).body(body);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<io.meimberg.licenses.web.dto.User> updateUser(
      @PathVariable("id") UUID id,
      @Valid @RequestBody UserUpdateRequest request
  ) {
    User updated = userService.update(id, request.getEmail(), request.getDisplayName(), request.getDepartmentId());
    return ResponseEntity.ok(userMapper.toDto(updated));
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }
}


