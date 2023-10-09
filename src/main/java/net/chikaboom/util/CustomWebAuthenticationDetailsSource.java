package net.chikaboom.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * Источник объекта деталей для объекта {@link org.springframework.security.core.Authentication}
 * Используется в настройках {@link net.chikaboom.config.WebSecurityConfig}
 */
@Component
public class CustomWebAuthenticationDetailsSource
        implements AuthenticationDetailsSource<HttpServletRequest, CustomWebAuthenticationDetails> {

    @Override
    public CustomWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new CustomWebAuthenticationDetails(context);
    }
}
