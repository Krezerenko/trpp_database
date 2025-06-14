package io.github.krezerenko.trpp_database.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException
{
    public UserAlreadyExistsException(String message)
    {
        super(message);
    }
}
