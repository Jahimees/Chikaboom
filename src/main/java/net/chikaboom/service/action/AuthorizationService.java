package net.chikaboom.service.action;

import net.chikaboom.model.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Класс реализует команду авторизации пользователя на сайте
 */
@PropertySource("/constants.properties")
@Service
public class AuthorizationService implements ActionService {

    @Value("${attr.id}")
    private String ID;
    @Value("${attr.email}")
    private String EMAIL;
    @Value("${page.main}")
    private String MAIN_PAGE;
    @Value("${page.account}")
    private String ACCOUNT_PAGE;
    Logger logger = Logger.getLogger(AuthorizationService.class);
    ClientDataStorageService clientDataStorageService;
    AccountRepository accountRepository;

    @Autowired
    public AuthorizationService(ClientDataStorageService clientDataStorageService, AccountRepository accountRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
    }

    /**
     * Реализация команды авторизации
     *
     * @return возвращает страницу пользователя в случае совпадения паролей,
     * возвращает стартовую страницу в случае нудачи.
     */
    @Override
    public String execute() {

        logger.info("Login procedure started");

        String email = clientDataStorageService.getData("email").toString();
        String password = clientDataStorageService.getData("password").toString();

        Account account = accountRepository.findByEmail(email);

        if (account != null && account.getPassword().equals(password)) {
            logger.info("User logged in");
            initSession((HttpServletRequest) clientDataStorageService.getData("servletRequest"), account);
            return ACCOUNT_PAGE;
        }

        logger.info("User has NOT logged in. Password or email is incorrect.");
        return MAIN_PAGE;
    }

    private void initSession(HttpServletRequest request, Account account) {

        HttpSession session = request.getSession();

        session.setAttribute(EMAIL, account.getEmail());
        session.setAttribute(ID, account.getIdAccount());
    }
}
