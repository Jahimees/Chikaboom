package net.chikaboom.service.action;

import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Сервис выхода из аккаунта
 */
@Service
@Transactional
public class LogoutActionService implements ActionService {

    @Value("${attr.phone}")
    private String PHONE;
    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.servletRequest}")
    private String SERVLET_REQUEST;
    @Value("${page.main}")
    private String MAIN_PAGE;

    private final Logger logger = Logger.getLogger(LogoutActionService.class);
    private final ClientDataStorageService clientDataStorageService;

    @Autowired
    public LogoutActionService(ClientDataStorageService clientDataStorageService) {
        this.clientDataStorageService = clientDataStorageService;
    }

    /**
     * Сбрасывает все параметры текущей сессии и возвращает ссылку на главную страницу
     *
     * @return ссылка на главную страницу
     */
    @Override
    public String executeAndGetPage() {

        HttpSession session = ((HttpServletRequest) clientDataStorageService.getData(SERVLET_REQUEST)).getSession();
        clientDataStorageService.dropData(SERVLET_REQUEST);

        session.invalidate();

        logger.info("User logged out successful");

        return MAIN_PAGE;
    }
}
