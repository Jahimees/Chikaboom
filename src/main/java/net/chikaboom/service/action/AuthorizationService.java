package net.chikaboom.service.action;

import net.chikaboom.dao.AccountDAO;
import net.chikaboom.model.Account;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.util.sql.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.util.constant.AttributeConstant.ID;
import static net.chikaboom.util.constant.FieldConstant.EMAIL;
import static net.chikaboom.util.constant.PageConstant.ACCOUNT_PAGE;
import static net.chikaboom.util.constant.PageConstant.MAIN_PAGE;

/**
 * Класс реализует команду авторизации пользователя на сайте
 */
@Service
public class AuthorizationService implements ActionService {

    Logger logger = Logger.getLogger(AuthorizationService.class);
    ClientDataStorageService clientDataStorageService;
    AccountDAO accountDAO;

    @Autowired
    public AuthorizationService(ClientDataStorageService clientDataStorageService, AccountDAO accountDAO) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountDAO = accountDAO;
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

        QueryBuilder builder = new QueryBuilder();

        String email = clientDataStorageService.getData("email").toString();
        String password = clientDataStorageService.getData("password").toString();

        List<String> parameters = new ArrayList<>();

        parameters.add(email);

        String query = builder.select().from("account").where("email").build();
        List<Account> accountList = accountDAO.executeQuery(query, parameters);

        Account account = accountList.get(0);

        if (account.getPassword().equals(password)) {
            logger.info("User logged in");
            initSession((HttpServletRequest) clientDataStorageService.getData("servletRequest"), account);
            return ACCOUNT_PAGE;
        } else {
            logger.info("User has NOT logged in. Password or email is incorrect.");
            return MAIN_PAGE;
        }
    }

    private void initSession(HttpServletRequest request, Account account) {

        HttpSession session = request.getSession();

        session.setAttribute(EMAIL, account.getEmail());
        session.setAttribute(ID, account.getIdAccount());
    }
}
