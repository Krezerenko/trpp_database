package io.github.krezerenko.trpp_database.api.users;

import jakarta.persistence.*;
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
    @Column(unique = true)
    private String email;
    @Column(unique = true, name = "phone_number", length = 20)
    private String phoneNumber;

    public void setPassword(String password, PasswordEncoder encoder)
    {
        this.passwordHash = encoder.encode(password);
    }
}
