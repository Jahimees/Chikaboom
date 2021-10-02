package net.chikaboom.servlet;

import net.chikaboom.commands.ActionCommand;
import net.chikaboom.commands.CommandFactory;
import net.chikaboom.commands.EmptyCommand;
import net.chikaboom.dao.AccountDAO;
import net.chikaboom.exception.UnknownCommandException;
import net.chikaboom.model.Account;
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
     * Пока куча грязи для тестов
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        AccountDAO accountDAO = new AccountDAO();

        Account account = new Account();
        account.setName("Alexander");
        account.setSurname("Anton");
        account.setLogin("Log In");
        account.setPassword("Psss word");
        account.setPhone("+4132124124144424");

        logger.info("TEST. Creating account...");
        accountDAO.create(account);
        logger.info("TEST. Account created");

        logger.info("TEST. Searching account...");
        Account foundAcc = accountDAO.findEntity(account.getIdAccount());
        logger.info("TEST. Account found");
        logger.info("TEST. Account: " + foundAcc.toString());

        logger.info("TEST. Updating account...");
        account.setPassword("fsdafa");
        accountDAO.update(account);
        logger.info("TEST. Account updated.");

        foundAcc = accountDAO.findEntity(account.getIdAccount());
        logger.info("TEST. Account: " + foundAcc);

        logger.info("TEST. Deleting account...");
        accountDAO.delete(account.getIdAccount());
        logger.info("TEST. Account deleted");
        logger.info("TEST. Account must be null = " + accountDAO.findEntity(account.getIdAccount()));


        logger.info("Приложение запущено!");
        logger.info("Открываю главную страницу...");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/main.jsp");
        requestDispatcher.forward(request, response);

        logger.info("Открыл главную страницу!");

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
