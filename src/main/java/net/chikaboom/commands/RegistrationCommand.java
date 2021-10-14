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

        logger.info("Регистрация ного аккаунта.");

        logger.info("Атрибут login: " + request.getParameter("login"));
        logger.info("Атрибут password: " + request.getParameter("password"));

        Account account = new Account();

        account.setLogin(request.getParameter("login"));
        account.setPassword(request.getParameter("password"));

        AccountDAO accountDAO = new AccountDAO();

        accountDAO.create(account);

        logger.info("Новый аккаунт создан");

        return ACCOUNT_PAGE;
    }
}
