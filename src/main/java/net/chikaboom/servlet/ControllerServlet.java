package net.chikaboom.servlet;

import net.chikaboom.commands.ActionCommand;
import net.chikaboom.commands.CommandFactory;
import net.chikaboom.commands.EmptyCommand;
import net.chikaboom.exception.UnknownCommandException;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.chikaboom.constant.AttributeConstant.COMMAND;
import static net.chikaboom.constant.PageConstant.MAIN_PAGE;

/**
 * Принимает сообщение от клиента и выполняет действия, заложенные в это сообщение
 */
public class ControllerServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ControllerServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Принимает абсолютно все запросы пользователя и выдает ему ответ
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String page;
        String commandName = request.getParameter(COMMAND);

        try {
            ActionCommand command = new CommandFactory().defineCommand(commandName);
            page = command.execute(request, response);
        } catch (UnknownCommandException e) {
            logger.error("Unknown command", e);
            page = new EmptyCommand().execute(request, response);
        }

        if (page != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } else {
            page = MAIN_PAGE;
            response.sendRedirect(request.getContextPath() + page);
        }
    }
}
