package io.github.krezerenko.trpp_database.api.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest
{
    private String refreshToken;
}
