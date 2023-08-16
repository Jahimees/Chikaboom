package net.chikaboom.controller.error.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Перехватчик неудачной попытки авторизации
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Перехватывает событие неудачной авторизации и отправляет соответствующее сообщение на клиент.
     *
     * @param request   запрос, который был отправлен во время аутентификации.
     * @param response  ответ сервера.
     * @param exception исключение, возникающее при попытке неудачного входа.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {
        logger.warn("Authentication failed: " + exception.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Authentication failed: " + exception.getMessage());
    }
}
