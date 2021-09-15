package net.chikaboom.commands;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCommand implements ActionCommand{

    Logger logger = Logger.getRootLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("LoginCommand: activated");

        // TODO realize command
        String loginPage = null;
        return loginPage;
    }
}
