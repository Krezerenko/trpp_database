package io.github.krezerenko.trpp_database;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<User>> getAllProducts()
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id)
    {
        User user = userService.findUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id)
    {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegistrationDto registrationDto)
    {
        UserResponseDto responseDto = userService.registerUser(registrationDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PostMapping("/login/name")
    public ResponseEntity<String> loginUserByName(@RequestParam String name, @RequestParam String password)
    {
        boolean isAuthenticated = userService.authenticateUser(name, password);
        return isAuthenticated ?
                ResponseEntity.ok("Login successful") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/login/email")
    public ResponseEntity<String> loginUserByEmail(@RequestParam String email, @RequestParam String password)
    {
        boolean isAuthenticated = userService.authenticateUser(email, password);
        return isAuthenticated ?
                ResponseEntity.ok("Login successful") :
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
