package net.chikaboom.commands;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.chikaboom.constant.PageConstant.ACCOUNT_PAGE;

/**
 * Класс реализует команду авторизации пользователя на сайте
 */
public class AuthorizationCommand implements ActionCommand {

    Logger logger = Logger.getLogger(ActionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("Пользователь начал авторизацию");

        logger.info("Пользователь успешно авторизовался.");

        return ACCOUNT_PAGE;
    }
}
