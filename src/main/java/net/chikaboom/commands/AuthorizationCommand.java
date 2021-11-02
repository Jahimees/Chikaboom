package net.chikaboom.commands;

import net.chikaboom.dao.AccountDAO;
import net.chikaboom.model.Account;
import net.chikaboom.util.QueryBuilder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.EMAIL;
import static net.chikaboom.constant.PageConstant.ACCOUNT_PAGE;
import static net.chikaboom.constant.PageConstant.MAIN_PAGE;

/**
 * Класс реализует команду авторизации пользователя на сайте
 */
public class AuthorizationCommand implements ActionCommand {

    Logger logger = Logger.getLogger(ActionCommand.class);
    private QueryBuilder builder = new QueryBuilder();

    private Account account;

    /**
     * Реализация команды авторизации
     *
     * @param request  запрос, получаемый со стороны клиента
     * @param response не используется
     * @return возвращает страницу пользователя в случае совпадения паролей,
     *         возвращает стартовую страницу в случае нудачи...
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("Login procedure has been started");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        List<String> emailList = new ArrayList<>();
        AccountDAO accountDAO = new AccountDAO();

        emailList.add(email);

        String query = builder.select().from("account").where("email").build();
        List<Account> accountList = accountDAO.executeQuery(query, emailList);

        account = accountList.get(0);

        if (account.getPassword().equals(password)) {
            logger.info("User has logged in");
            initSession(request, email);
            return ACCOUNT_PAGE;
        } else {
            logger.info("User has NOT logged in. Password is incorrect.");
            return MAIN_PAGE;
        }
    }

    private void initSession(HttpServletRequest request, String email) {

        HttpSession session = request.getSession();

        session.setAttribute(EMAIL, email);
        session.setAttribute("id", account.getIdAccount()); //КУДА ЗАПИСАТЬ КОНСТАНТУ ID???
    }
}
