package io.github.krezerenko.trpp_database.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter
{
    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils)
    {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws IOException, ServletException
    {
        String token = extractToken(request);
        if (token != null)
        {
            try
            {
                String username = jwtUtils.validateToken(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, List.of(new SimpleGrantedAuthority("USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);
                logger.debug("User " + username + " authenticated successfully with JWT.");
            }
            catch (JwtException e)
            {
                logger.warn("Invalid JWT token: " + e.getMessage());
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"INVALID_TOKEN\", \"message\": \"Invalid JWT token: " + e.getMessage() + "\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request)
    {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer "))
        {
            return header.substring(7);
        }
        logger.trace("Authorization header is missing or does not start with 'Bearer '");
        return null;
    }
}
