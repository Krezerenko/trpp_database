package io.github.krezerenko.trpp_database.api.users;

import lombok.Data;

@Data
public class UserRequestDto
{
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
}
