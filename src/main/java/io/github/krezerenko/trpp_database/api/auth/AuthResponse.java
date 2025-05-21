package io.github.krezerenko.trpp_database.api.auth;

import lombok.Data;

@Data
public class AuthResponse
{
    private String accessToken;
    private String refreshToken;
    private String message;

    public AuthResponse(String accessToken, String refreshToken)
    {
        this.accessToken = accessToken;
    }

    public AuthResponse(String message, boolean isError)
    {
        this.message = message;
    }
}
