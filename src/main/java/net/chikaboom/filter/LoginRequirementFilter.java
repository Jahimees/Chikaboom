package net.chikaboom.filter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static net.chikaboom.util.constant.CommandConstant.AUTHORIZATION;
import static net.chikaboom.util.constant.CommandConstant.REGISTRATION;

/**
 * Класс-фильтр. Предназанчен для проверки запрашиваемой страницы на необходимость авторизации со стороны пользователя.
 * Если страница не требует авторизации - передаем управление ServletController;
 * Если страница требует авторизации:
 * - если пользователь авторизован - передаем управление ServletController;
 * - если пользователь НЕ авторизован - перенаправляем на страницу авторизации.
 */
//TODO Перевести на SPRING
@Deprecated
@PropertySource("/constants.properties")
public class LoginRequirementFilter implements Filter {

    @Value("${attr.command}")
    private String COMMAND;
    @Value("${page.main}")
    private String MAIN_PAGE;
    @Value("${page.auth_popup}")
    private String AUTHORIZATION_POPUP;
    Logger logger = Logger.getLogger(LoginRequirementFilter.class);

    /**
     * Метод инициализации фильтра перед запуском приложения
     *
     * @param filterConfig передаваемая уконфигурация
     */
    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("Filter initiated");
    }

    /**
     * Метод реализации фильтра. Проверяет страницу на необходимость авторизации со стороны пользователя
     *
     * @param request     запрос от клиента
     * @param response    не используется
     * @param filterChain цепочка фильтров
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        logger.info("Filtration started");

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = ((HttpServletRequest) request).getSession();

        String path = servletRequest.getServletPath();
        String id = session.getId();
        String command = servletRequest.getParameter(COMMAND);

        logger.info("'" + command + "' has been got");

        if (!isCommonPath(path) && (id.equals("") || isAllowedCommand(command))) {
            logger.info("Restricted page. User need to authorize.");
            logger.info("User is redirecting to login page.");

            servletResponse.sendRedirect(AUTHORIZATION_POPUP);
        } else {
            logger.info("Allowed page. Passing to ControllerServlet");

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * Метод для проверки передаваемого клиентом пути. Если путь в списке разрешенных без авторизации - возвращает TRUE
     *
     * @param path путь передаваемый клиентом
     * @return true или false в зависимости от того, какой был получен путь
     *
     * TODO необходимо дополнить страницы
     */
    private boolean isCommonPath(String path) {
        switch (path) {
            case "/view/main.jsp":
                return true;
            default:
                return false;
        }
    }

    /**
     * Метод для проверки передаваемой клиентом команды. Если команда в списке разрешенных без авторизации -
     * возвращает TRUE.
     *
     * @param command передаваемая клиентом команда
     * @return true или false в зависимости от того, какая была получена команда
     *
     * TODO необходимо дополнить команды
     */
    private boolean isAllowedCommand(String command) {
        switch (command) {
            case REGISTRATION:
            case AUTHORIZATION:
                return true;
            default:
                return false;
        }
    }
}
