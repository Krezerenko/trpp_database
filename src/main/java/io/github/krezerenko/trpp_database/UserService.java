package io.github.krezerenko.trpp_database;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }
    public User findUserById(Long id)
    {
        return userRepository.findById(id).orElse(null);
    }
    public User findUserByName(String name)
    {
        return userRepository.findByName(name).orElse(null);
    }
    public User saveUser(User user)
    {
        return userRepository.save(user);
    }
    public void deleteUserById(Long id)
    {
        userRepository.deleteById(id);
    }
    public UserResponseDto registerUser(UserRegistrationDto userDto)
    {
        // Check if username/email already exists
        if (userRepository.existsByName(userDto.getName())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        // Convert DTO to User entity and hash password
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        // Save to database
        User savedUser = userRepository.save(user);

        // Convert saved User entity to Response DTO
        return convertToResponseDto(savedUser);
    }
    public UserResponseDto convertToResponseDto(User user)
    {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        return responseDto;
    }

    public boolean authenticateUser(String name, String password)
    {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return passwordEncoder.matches(password, user.getPasswordHash());
    }
}
