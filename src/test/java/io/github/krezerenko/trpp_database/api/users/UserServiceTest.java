package io.github.krezerenko.trpp_database.api.users;

import io.github.krezerenko.trpp_database.api.auth.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setName("testuser");
        testUser.setPasswordHash("hashedpassword");
    }

    @Test
    void testFindUserById_Success() {
        // Given
        UUID userId = testUser.getId();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindUserById_NotFound() {
        // Given
        UUID nonExistingId = UUID.randomUUID();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // When
        User result = userService.findUserById(nonExistingId);

        // Then
        assertNull(result);
        verify(userRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void testFindUserByName_Success() {
        // Given
        String username = testUser.getName();
        when(userRepository.findByName(anyString())).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findUserByName(username);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findByName(username);
    }

    @Test
    void testFindUserByName_NotFound() {
        // Given
        String nonExistingName = "nonexistentuser";
        when(userRepository.findByName(anyString())).thenReturn(Optional.empty());

        // When
        User result = userService.findUserByName(nonExistingName);

        // Then
        assertNull(result);
        verify(userRepository, times(1)).findByName(nonExistingName);
    }

    @Test
    void testSaveUser_Success() {
        // Given
        User userToSave = new User();
        userToSave.setName("newuser");
        userToSave.setPasswordHash("hashedpassword");
        
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.saveUser(userToSave);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void testDeleteUserById_Success() {
        // Given
        UUID userId = testUser.getId();

        // When
        userService.deleteUserById(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }
}
