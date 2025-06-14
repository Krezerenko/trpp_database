package io.github.krezerenko.trpp_database.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidCredentialsException extends RuntimeException
{
    public InvalidCredentialsException(String message)
    {
        super(message);
    }
}
