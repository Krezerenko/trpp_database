package io.github.krezerenko.trpp_database.api.auth;

import io.github.krezerenko.trpp_database.api.users.User;
import io.github.krezerenko.trpp_database.api.users.UserRepository;
import io.github.krezerenko.trpp_database.api.users.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService
{
    private final UserRepository userRepository;
    public static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User registerUser(UserRegistrationDto userDto)
    {
        if (userRepository.existsByName(userDto.getName()))
        {
            throw new UserAlreadyExistsException("Username already taken");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    public User authenticateUser(String username, String password)
    {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User " + username + " is not found"));
        if (!passwordEncoder.matches(password, user.getPasswordHash()))
        {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return user;
    }

    public static boolean matchPassword(String password, String passwordHash)
    {
        return passwordEncoder.matches(password, passwordHash);
    }
}
