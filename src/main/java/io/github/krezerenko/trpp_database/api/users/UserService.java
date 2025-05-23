package io.github.krezerenko.trpp_database.api.users;

import io.github.krezerenko.trpp_database.api.auth.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public static UserResponseDto convertToResponseDto(User user)
    {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setName(user.getName());
        responseDto.setEmail(user.getEmail());
        responseDto.setPhoneNumber(user.getPhoneNumber());
        return responseDto;
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
    public User findUserByEmail(String email)
    {
        return userRepository.findByEmail(email).orElse(null);
    }
    public User saveUser(User user)
    {
        return userRepository.save(user);
    }
    public void deleteUserById(Long id)
    {
        userRepository.deleteById(id);
    }
}
