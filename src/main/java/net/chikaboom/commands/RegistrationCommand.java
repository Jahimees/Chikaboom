package net.chikaboom.commands;

import net.chikaboom.dao.AccountDAO;
import net.chikaboom.model.Account;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.chikaboom.constant.PageConstant.ACCOUNT_PAGE;

/**
 * Класс реализует команду создания нового аккаунта
 */
public class RegistrationCommand implements ActionCommand {

    Logger logger = Logger.getLogger(RegistrationCommand.class);

    /**
     * Реализация команды регистрации нового аккаунта
     * @param request запрос, пришедший со стороны клиента
     * @param response не используется
     * @return возвращает страницу аккаунта (страницу заполнения личных данных?)
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("New account creating has been started.");
        logger.info("Attribute email: " + request.getParameter("email"));

        Account account = new Account();

        account.setEmail(request.getParameter("email"));
        account.setPassword(request.getParameter("password"));

        AccountDAO accountDAO = new AccountDAO();

        accountDAO.create(account);

        logger.info("New account has been created");

        return ACCOUNT_PAGE;
    }
}
