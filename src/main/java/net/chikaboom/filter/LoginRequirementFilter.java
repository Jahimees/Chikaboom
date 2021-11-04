package net.chikaboom.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static net.chikaboom.constant.AttributeConstant.COMMAND;
import static net.chikaboom.constant.CommandConstant.AUTHORIZATION;
import static net.chikaboom.constant.CommandConstant.REGISTRATION;
import static net.chikaboom.constant.PageConstant.*;

/**
 * Класс-фильтр. Предназанчен для проверки запрашиваемой страницы на необходимость авторизации со стороны пользователя.
 * Если страница не требует авторизации - передаем управление ServletController;
 * Если страница требует авторизации:
 * - если пользователь авторизован - передаем управление ServletController;
 * - если пользователь НЕ авторизован - перенаправляем на страницу (вызываем попап) авторизации.
 */
public class LoginRequirementFilter implements Filter {

    Logger logger = Logger.getLogger(LoginRequirementFilter.class);

    /**
     * Метод инициализации фильтра перед запуском приложения
     * @param filterConfig передаваемая уконфигурация
     * @throws ServletException
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Filter initiated");
    }

    /**
     * Метод реализации фильтра. Проверяет страницу на необходимость авторизации со стороны пользователя
     * @param request запрос от клиента
     * @param response не используется
     * @param filterChain цепочка вильтров? (так что ли?)
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        logger.info("Filtration started");

        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpSession session = ((HttpServletRequest) request).getSession();

        String path = servletRequest.getServletPath();
        String id = session.getId();
        String command = servletRequest.getParameter(COMMAND);

        logger.info("\'" + command + "\' has been got");

        boolean isCommonPath = checkPath(path);

        if (!isCommonPath && (id == null || id == "" || checkCommand(command))) {
            logger.info("Restricted page. User need to authorize.");
            logger.info("User is redirecting to loin popup.");

            servletResponse.sendRedirect(AUTHORIZATION_POPUP);
        } else {
            logger.info("Allowed page. Passing to ControllerServlet");

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * Выводит фильтр из эксплуатации
     */
    public void destroy() {

    }

    /**
     * Метод для проверки передаваемого клиентом пути
     * @param path путь передаваемый клиентом
     * @return true или false в зависимости от условия
     *
     * TODO необходимо дополнить страницы
     */
    private boolean checkPath(String path) {
        switch (path) {
            case MAIN_PAGE:
            case ACCOUNT_PAGE:
                return true;
            default: return false;
        }
    }

    /**
     * Метод для проверки передаваемой клиентом команды
     * @param command передаваемая клиентом команда
     * @return true или false в зависимости от условия
     *
     * TODO необходимо дополнить команды
     */
    private boolean checkCommand(String command) {
        switch (command) {
            case REGISTRATION:
            case AUTHORIZATION:
                return true;
            default: return false;
        }
    }
}
