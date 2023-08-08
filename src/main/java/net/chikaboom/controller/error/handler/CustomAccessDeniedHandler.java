package net.chikaboom.controller.error.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Перехватчик событий попыток доступа к неразрешённому ресурсу
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        logger.warn(accessDeniedException.getMessage());
        response.sendRedirect("/403");
    }
}
