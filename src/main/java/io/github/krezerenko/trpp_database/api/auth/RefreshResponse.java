package io.github.krezerenko.trpp_database.api.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshResponse
{
    String accessToken;
}
