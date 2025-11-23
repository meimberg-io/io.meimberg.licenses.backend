package io.meimberg.licenses.service;

import io.meimberg.licenses.domain.User;
import io.meimberg.licenses.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private UUID userId;
  private User user;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    user = new User();
    user.setId(userId);
    user.setEmail("test@example.com");
    user.setDisplayName("Test User");
  }

  @Test
  void get_success() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> result = userService.get(userId);

    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(user);
    verify(userRepository).findById(userId);
  }

  @Test
  void get_notFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    Optional<User> result = userService.get(userId);

    assertThat(result).isEmpty();
  }

  @Test
  void list() {
    User user2 = new User();
    user2.setId(UUID.randomUUID());
    user2.setEmail("user2@example.com");
    user2.setDisplayName("User 2");

    when(userRepository.findAll()).thenReturn(List.of(user, user2));

    List<User> result = userService.list();

    assertThat(result).hasSize(2);
    assertThat(result).containsExactly(user, user2);
    verify(userRepository).findAll();
  }

  @Test
  void search_withEmail() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<User> page = new PageImpl<>(List.of(user), pageable, 1);

    when(userRepository.findByEmailContainingIgnoreCase("test", pageable)).thenReturn(page);

    Page<User> result = userService.search("test", pageable);

    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0)).isEqualTo(user);
    verify(userRepository).findByEmailContainingIgnoreCase("test", pageable);
    verify(userRepository, never()).findAll(any(Pageable.class));
  }

  @Test
  void search_withBlankEmail() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<User> page = new PageImpl<>(List.of(user), pageable, 1);

    when(userRepository.findAll(pageable)).thenReturn(page);

    Page<User> result = userService.search("  ", pageable);

    assertThat(result.getContent()).hasSize(1);
    verify(userRepository).findAll(pageable);
    verify(userRepository, never()).findByEmailContainingIgnoreCase(any(), any());
  }

  @Test
  void search_withNullEmail() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<User> page = new PageImpl<>(List.of(user), pageable, 1);

    when(userRepository.findAll(pageable)).thenReturn(page);

    Page<User> result = userService.search(null, pageable);

    assertThat(result.getContent()).hasSize(1);
    verify(userRepository).findAll(pageable);
    verify(userRepository, never()).findByEmailContainingIgnoreCase(any(), any());
  }

  @Test
  void create() {
    String email = "new@example.com";
    String displayName = "New User";

    when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
      User u = invocation.getArgument(0);
      u.setId(userId);
      return u;
    });

    User result = userService.create(email, displayName);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(userId);
    assertThat(result.getEmail()).isEqualTo(email);
    assertThat(result.getDisplayName()).isEqualTo(displayName);
    verify(userRepository).save(any(User.class));
  }

  @Test
  void update_success() {
    String newEmail = "updated@example.com";
    String newDisplayName = "Updated User";

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    User result = userService.update(userId, newEmail, newDisplayName);

    assertThat(result.getEmail()).isEqualTo(newEmail);
    assertThat(result.getDisplayName()).isEqualTo(newDisplayName);
    verify(userRepository).save(user);
  }

  @Test
  void update_partial() {
    String newEmail = "updated@example.com";

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);

    User result = userService.update(userId, newEmail, null);

    assertThat(result.getEmail()).isEqualTo(newEmail);
    assertThat(result.getDisplayName()).isEqualTo(user.getDisplayName());
    verify(userRepository).save(user);
  }

  @Test
  void update_notFound() {
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.update(userId, "new@example.com", "New Name"))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("User not found");

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void delete_success() {
    when(userRepository.existsById(userId)).thenReturn(true);

    userService.delete(userId);

    verify(userRepository).deleteById(userId);
  }

  @Test
  void delete_notFound() {
    when(userRepository.existsById(userId)).thenReturn(false);

    assertThatThrownBy(() -> userService.delete(userId))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessageContaining("User not found");

    verify(userRepository, never()).deleteById(any());
  }
}



