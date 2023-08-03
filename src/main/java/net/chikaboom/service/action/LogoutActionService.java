package net.chikaboom.service.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис выхода из аккаунта
 */
@Service
@Transactional
public class LogoutActionService {

    @Value("${page.main}")
    private String MAIN_PAGE;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Сбрасывает все параметры текущей сессии и возвращает ссылку на главную страницу
     *
     * @return ссылка на главную страницу
     */
//    TODO FIXME NEW не должно быть сессии?
    public String logout(HttpServletRequest request) {

        HttpSession session = request.getSession();

        session.invalidate();

        logger.info("User logged out successful");

        return MAIN_PAGE;
    }
}
