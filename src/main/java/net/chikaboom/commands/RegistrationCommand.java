package net.chikaboom.commands;

import net.chikaboom.dao.AccountDAO;
import net.chikaboom.model.Account;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static net.chikaboom.constant.PageConstant.ACCOUNT_PAGE;

public class RegistrationCommand implements ActionCommand{

    Logger logger = Logger.getLogger(RegistrationCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("Creating new account.");

        logger.info("attr login: " + request.getParameter("login"));
        logger.info("attr password: " + request.getParameter("password"));

        Account account = new Account();

        account.setLogin(request.getParameter("login"));
        account.setPassword(request.getParameter("password"));

        AccountDAO accountDAO = new AccountDAO();

        accountDAO.create(account);

        return ACCOUNT_PAGE;
    }
}
