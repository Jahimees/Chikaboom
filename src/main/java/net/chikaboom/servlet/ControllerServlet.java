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
     * Пока куча грязи для тестов
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        AccountDAO accountDAO = new AccountDAO();
//
//        Account account = new Account();
//        account.setName("Alexander");
//        account.setSurname("Anton");
//        account.setLogin("Log In");
//        account.setPassword("Psss word");
//        account.setPhone("+4132124124144424");
//
//        logger.info("TEST. Creating account...");
//        accountDAO.create(account);
//        logger.info("TEST. Account created");
//
//        QueryBuilder queryBuilder = new QueryBuilder();
//        String query = queryBuilder.select().from(TableConstant.ACCOUNT).where().compare(FieldConstant.LOGIN, SqlComparatorType.EQUAL).build();
//        String login = "Log in";
//
//        List<Account> accountList = accountDAO.executeQuery(query, Arrays.asList(login));
//
//        logger.info("Полученные штуки: ");
//        accountList.forEach(account1 -> logger.info(account1.toString()));
//
//
//        logger.info("TEST. Deleting account...");
//        accountDAO.delete(account.getIdAccount());
//        logger.info("TEST. Account deleted");
//        logger.info("TEST. Account must be null = " + accountDAO.findEntity(account.getIdAccount()));

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
