package net.chikaboom.commands;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterCommand implements ActionCommand{

    Logger logger = Logger.getRootLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        logger.info("RegisterCommand: activated");

        // TODO realize command
        String registerPage = null;
        return registerPage;
    }
}
