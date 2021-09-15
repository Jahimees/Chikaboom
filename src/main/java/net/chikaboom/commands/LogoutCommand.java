package net.chikaboom.commands;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand implements ActionCommand{

    Logger logger = Logger.getRootLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("LogoutCommand: activated");

        // TODO realize command
        String logoutPage = null;
        return logoutPage;
    }
}
