package net.chikaboom.service.action;

import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
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

    @Override
    public String execute() {

        HttpSession session = ((HttpServletRequest) clientDataStorageService.getData(SERVLET_REQUEST)).getSession();
        clientDataStorageService.dropData(SERVLET_REQUEST);

        session.setAttribute(PHONE, null);
        session.setAttribute(ID_ACCOUNT, null);

        logger.info("User logged out successful");

        return MAIN_PAGE;
    }
}
