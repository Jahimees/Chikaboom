package net.chikaboom.servlet;

import net.chikaboom.dao.AccountDAO;
import net.chikaboom.model.Account;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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
        account.setPhone("+4132124124124");

        accountDAO.create(account);
        logger.info("Account " + account.getIdAccount() + " created!");

        List<Account> accountList = accountDAO.findAll();
        logger.info("Found accs:");
        for (Account acc : accountList) {
            logger.info(acc.toString());
        }

        logger.info("\n\n");
        logger.info("Finding acc with id " + account.getIdAccount() + "...");
        logger.info("Found  account: " + accountDAO.findEntity(account.getIdAccount()).toString() + "\n");

        logger.info("Updating account " + account.getIdAccount() + "...");
        account.setLogin("Logggen");
        accountDAO.update(account);
        logger.info("Checking for updates: " + accountDAO.findEntity(account.getIdAccount()).toString() + "\n\n");

        logger.info("Deleting account " + account.getIdAccount() + "...");
        accountDAO.delete(account.getIdAccount());
        logger.info("Trying to find account after deleteng...");
        logger.info("Must be null: " + accountDAO.findEntity(account.getIdAccount()));


        logger.info("Приложение запущено!");
        logger.info("Открываю главную страницу...");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/main.jsp");
        requestDispatcher.forward(request, response);
        logger.info("Открыл главную страницу!");
    }
}
