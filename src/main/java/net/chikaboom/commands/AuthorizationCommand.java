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

import static net.chikaboom.constant.AttributeConstant.ID;
import static net.chikaboom.constant.FieldConstant.EMAIL;
import static net.chikaboom.constant.PageConstant.ACCOUNT_PAGE;
import static net.chikaboom.constant.PageConstant.MAIN_PAGE;

/**
 * Класс реализует команду авторизации пользователя на сайте
 */
public class AuthorizationCommand implements ActionCommand {

    Logger logger = Logger.getLogger(ActionCommand.class);

    //Убрать многоточие после "неудачи"
    /**
     * Реализация команды авторизации
     *
     * @param request  запрос, получаемый со стороны клиента
     * @param response не используется
     * @return возвращает страницу пользователя в случае совпадения паролей,
     * возвращает стартовую страницу в случае нудачи...
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("Login procedure has been started");

        QueryBuilder builder = new QueryBuilder();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        List<String> emailList = new ArrayList<>(); //Переименовать на parameters
        AccountDAO accountDAO = new AccountDAO();

        emailList.add(email);

        String query = builder.select().from("account").where("email").build();
        List<Account> accountList = accountDAO.executeQuery(query, emailList);

        Account account = accountList.get(0);

        if (account.getPassword().equals(password)) {
            logger.info("User has logged in");
            initSession(request, account);
            return ACCOUNT_PAGE;
        } else {
            logger.info("User has NOT logged in. Password is incorrect."); //Password or email
            return MAIN_PAGE;
        }
    }

    private void initSession(HttpServletRequest request, Account account) {

        HttpSession session = request.getSession();

        session.setAttribute(EMAIL, account.getEmail());
        session.setAttribute(ID, account.getIdAccount());
    }
}
