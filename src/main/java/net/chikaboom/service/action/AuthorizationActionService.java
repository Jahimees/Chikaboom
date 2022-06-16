package net.chikaboom.service.action;

import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import net.chikaboom.util.constant.RequestParametersConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static net.chikaboom.util.constant.RequestParametersConstant.PASSWORD;
import static net.chikaboom.util.constant.RequestParametersConstant.SERVLET_REQUEST;

/**
 * Сервис реализует авторизацию пользователя на сайте
 */
@Service
public class AuthorizationActionService implements ActionService {

    @Value("${attr.id}")
    private String ID;
    @Value("${attr.email}")
    private String EMAIL;
    @Value("${page.account}")
    private String ACCOUNT_PAGE;

    private final Logger logger = Logger.getLogger(AuthorizationActionService.class);
    private final ClientDataStorageService clientDataStorageService;
    private final HashPasswordService hashPasswordService;
    private final AccountRepository accountRepository;

    @Autowired
    public AuthorizationActionService(ClientDataStorageService clientDataStorageService, AccountRepository accountRepository,
                                      HashPasswordService hashPasswordService) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
        this.hashPasswordService = hashPasswordService;
    }

    /**
     * Реализация команды авторизации
     *
     * @return возвращает страницу пользователя в случае совпадения паролей,
     * выбрасывает ошибку некорректного ввода данных в случае нудачи.
     */
    @Override
    public String execute() {
        logger.info("Login procedure started");

        String email = clientDataStorageService.getData(RequestParametersConstant.EMAIL).toString();
        clientDataStorageService.dropData(RequestParametersConstant.EMAIL);

        Account account = accountRepository.findOneByEmail(email);

        if (account != null) {
            String actualPassword = hashPasswordService.convertPasswordForComparing(
                    clientDataStorageService.getData(PASSWORD).toString(), account.getSalt());
            clientDataStorageService.dropData(PASSWORD);

            if (account.getPassword().equals(actualPassword)) {
                logger.info("User logged in");
                initSession((HttpServletRequest) clientDataStorageService.getData(SERVLET_REQUEST), account);
                clientDataStorageService.dropData(SERVLET_REQUEST);

                return ACCOUNT_PAGE;
            }
        }

        logger.info("User has NOT logged in. Password or email is incorrect.");

        throw new IncorrectInputDataException("Email and/or password are/is incorrect");
    }

    private void initSession(HttpServletRequest request, Account account) {
        HttpSession session = request.getSession();

        session.setAttribute(EMAIL, account.getEmail());
        session.setAttribute(ID, account.getIdAccount());
    }
}
