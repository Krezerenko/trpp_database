package io.github.krezerenko.trpp_database.api.auth;

import io.github.krezerenko.trpp_database.api.users.User;
import io.github.krezerenko.trpp_database.api.users.UserService;
import io.github.krezerenko.trpp_database.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final AuthService authService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService, JwtUtils jwtUtils)
    {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRegistrationDto registrationDto)
    {
        User user = authService.registerUser(registrationDto);

        String accessToken = jwtUtils.generateAccessToken(user.getId().toString());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId().toString());
        return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUserByName(@RequestBody LoginRequest request)
    {
        User user = authService.authenticateUser(request.getName(), request.getPassword());

        String accessToken = jwtUtils.generateAccessToken(user.getId().toString());
        String refreshToken = jwtUtils.generateRefreshToken(user.getId().toString());
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken()
    {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        String newAccessToken = jwtUtils.generateAccessToken(id);
        return ResponseEntity.ok(new RefreshResponse(newAccessToken));
    }
}
