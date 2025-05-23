package io.github.krezerenko.trpp_database.api.users;

import io.github.krezerenko.trpp_database.api.auth.AuthService;
import io.github.krezerenko.trpp_database.api.auth.UserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id)
    {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me")
    public ResponseEntity<UserResponseDto> saveUserByToken(@RequestBody UserRequestDto newUser)
    {
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("Saving user with id {}", id);
        User user = userService.findUserById(id);
        if (user == null)
        {
            return ResponseEntity.badRequest().build();
        }
        if (!AuthService.matchPassword(newUser.getPassword(), user.getPasswordHash()))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPhoneNumber(newUser.getPhoneNumber());
        return ResponseEntity.ok(UserService.convertToResponseDto(userService.saveUser(user)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getUserByToken()
    {
        Long id = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        log.info("User ID: {}", id);
        User user = userService.findUserById(id);
        if (user == null)
        {
            return ResponseEntity.badRequest().build();
        }
        UserResponseDto response = UserService.convertToResponseDto(user);
        return ResponseEntity.ok(response);
    }
}
