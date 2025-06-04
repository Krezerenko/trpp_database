package io.github.krezerenko.trpp_database.api.auth;

import io.github.krezerenko.trpp_database.api.users.User;
import io.github.krezerenko.trpp_database.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private final String TEST_ACCESS_TOKEN = "test-access-token";
    private final String TEST_REFRESH_TOKEN = "test-refresh-token";
    private final String TEST_USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.fromString(TEST_USER_ID));
        testUser.setName("testuser");
        testUser.setPasswordHash("hashedpassword");

        // Reset mocks
        reset(authService, jwtUtils, passwordEncoder);
    }

    @Test
    void registerUser_ValidRegistration_ReturnsTokens() {
        // Arrange
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setName("newuser");
        registrationDto.setPassword("password123");

        when(authService.registerUser(any(UserRegistrationDto.class))).thenReturn(testUser);
        when(jwtUtils.generateAccessToken(TEST_USER_ID)).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtUtils.generateRefreshToken(TEST_USER_ID)).thenReturn(TEST_REFRESH_TOKEN);

        // Act
        ResponseEntity<AuthResponse> response = authController.registerUser(registrationDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_ACCESS_TOKEN, response.getBody().getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getBody().getRefreshToken());

        verify(authService).registerUser(registrationDto);
        verify(jwtUtils).generateAccessToken(TEST_USER_ID);
        verify(jwtUtils).generateRefreshToken(TEST_USER_ID);
    }

    @Test
    void loginUser_ValidCredentials_ReturnsTokens() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");
        
        when(authService.authenticateUser("testuser", "password123")).thenReturn(testUser);
        when(jwtUtils.generateAccessToken(TEST_USER_ID)).thenReturn(TEST_ACCESS_TOKEN);
        when(jwtUtils.generateRefreshToken(TEST_USER_ID)).thenReturn(TEST_REFRESH_TOKEN);

        // Act
        ResponseEntity<AuthResponse> response = authController.loginUserByName(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_ACCESS_TOKEN, response.getBody().getAccessToken());
        assertEquals(TEST_REFRESH_TOKEN, response.getBody().getRefreshToken());

        verify(authService).authenticateUser("testuser", "password123");
        verify(jwtUtils).generateAccessToken(TEST_USER_ID);
        verify(jwtUtils).generateRefreshToken(TEST_USER_ID);
    }

    @Test
    void refreshToken_ValidToken_ReturnsNewAccessToken() {
        // Arrange
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            TEST_USER_ID, 
            null, 
            Collections.emptyList()
        );
        
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(jwtUtils.generateAccessToken(TEST_USER_ID)).thenReturn("new-access-token");

        // Act
        ResponseEntity<RefreshResponse> response = authController.refreshToken();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("new-access-token", response.getBody().getAccessToken());
        
        verify(jwtUtils).generateAccessToken(TEST_USER_ID);
    }
}
