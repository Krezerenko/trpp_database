package io.github.krezerenko.trpp_database.api.users;

import io.github.krezerenko.trpp_database.api.auth.AuthService;
import io.github.krezerenko.trpp_database.api.auth.InvalidCredentialsException;
import io.github.krezerenko.trpp_database.api.auth.UserNotFoundException;
import io.github.krezerenko.trpp_database.api.auth.UserResponseDto;
import io.github.krezerenko.trpp_database.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers()
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUserByToken()
    {
        UUID id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userService.findUserById(id);
        if (user == null)
        {
            throw new UserNotFoundException("User not found");
        }
        UserResponseDto response = UserService.convertToResponseDto(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/me")
    public ResponseEntity<UserResponseDto> saveUserByToken(@RequestBody UserRequestDto newUser)
    {
        UUID id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userService.findUserById(id);
        checkUserRequestValid(newUser.getPassword(), user);
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPhoneNumber(newUser.getPhoneNumber());
        return ResponseEntity.ok(UserService.convertToResponseDto(userService.saveUser(user)));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUserByToken()
    {
        UUID id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/me/password")
    public ResponseEntity<Void> confirmPasswordByToken(@RequestBody PasswordDto request)
    {
        UUID id = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userService.findUserById(id);
        checkUserRequestValid(request.getPassword(), user);
        return ResponseEntity.ok().build();
    }

    private static void checkUserRequestValid(String password, User user)
    {
        if (user == null)
        {
            throw new UserNotFoundException("User not found");
        }
        if (!AuthService.matchPassword(password, user.getPasswordHash()))
        {
            throw new InvalidCredentialsException("Invalid password");
        }
    }
}
