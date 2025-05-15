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
    public User registerUser(String name, String password)
    {
        User user = new User();
        user.setName(name);
        user.setPassword(password, passwordEncoder);
        return saveUser(user);
    }
    public boolean authenticateUser(String name, String password)
    {
        User user = userRepository.findByName(name).orElse(null);
        return user != null && passwordEncoder.matches(password, user.getPasswordHash());
    }
}
