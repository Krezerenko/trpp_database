package io.github.krezerenko.trpp_database.api.auth;

import lombok.Data;

@Data
public class UserResponseDto
{
    private String name;
    private String email;
    private String phoneNumber;
}
