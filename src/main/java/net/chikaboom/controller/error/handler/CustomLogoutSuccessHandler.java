package net.chikaboom.controller.error.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.chikaboom.util.CustomWebAuthenticationDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Перехватчик события успешного выхода из аккаунта. Предназначен для определения откуда поступил запрос: с UI или через api
 */
@Component
public class CustomLogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * В случае успешного процесса выхода из аккаунта, определяет, откуда пришёл запрос.
     * Если запрос пришёл с UI - перенаправляет пользователя на главную страницу.
     * Если запрос пришёл не через UI - отправляет json-ответ
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (((CustomWebAuthenticationDetails) authentication.getDetails()).isRequestFromUI()) {
            redirectStrategy.sendRedirect(request, response, "/chikaboom/main");
        } else {
            redirectStrategy.sendRedirect(request, response, "/success/logout");
        }
    }
}
