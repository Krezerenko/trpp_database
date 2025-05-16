package io.github.krezerenko.trpp_database;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
    @Column(nullable = false, name = "password_hash")
    private String passwordHash;
    @Column(nullable = false, unique = true)
    private String email;

    public void setPassword(String password, PasswordEncoder encoder)
    {
        this.passwordHash = encoder.encode(password);
    }
}
