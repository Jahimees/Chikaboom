package net.chikaboom.servlet;

import net.chikaboom.dao.AccountDAO;
import net.chikaboom.model.Account;
import net.chikaboom.util.SqlComparatorType;
import net.chikaboom.util.SqlOperandType;
import net.chikaboom.util.SqlWhereEntity;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
        account.setIdAccount("508748ef-f33b-4040-8d89-56b5b46a789b");

        SqlWhereEntity ewe = new SqlWhereEntity();
        Map<String, SqlComparatorType> fields = new LinkedHashMap<>();

        fields.put("login", SqlComparatorType.EQUAL);
        fields.put("password", SqlComparatorType.EQUAL);
        fields.put("cost", SqlComparatorType.GRATER);

//        where login=? AND password=? OR cost>?

        Queue<SqlOperandType> operandTypes = new LinkedList<>();
        operandTypes.add(SqlOperandType.AND);
        operandTypes.add(SqlOperandType.OR);

        ewe.setFields(fields);
        ewe.setOperands(operandTypes);


//        accountDAO.create(account);
//        logger.info("Account " + account.getIdAccount() + " created!");
//
//        List<Account> accountList = accountDAO.findAll();
//        logger.info("Found accs:");
//        for (Account acc : accountList) {
//            logger.info(acc.toString());
//        }

        logger.info("\n\n");
        logger.info("Finding acc with id " + account.getIdAccount() + "...");
        logger.info("Found  account: " + accountDAO.findEntity(account.getIdAccount()).toString() + "\n");
//
//        logger.info("Updating account " + account.getIdAccount() + "...");
//        account.setLogin("Logggen");
//        accountDAO.update(account);
//        logger.info("Checking for updates: " + accountDAO.findEntity(account.getIdAccount()).toString() + "\n\n");

//        logger.info("Deleting account " + account.getIdAccount() + "...");
//        accountDAO.delete(account.getIdAccount());
//        logger.info("Trying to find account after deleteng...");
//        logger.info("Must be null: " + accountDAO.findEntity(account.getIdAccount()));


        logger.info("Приложение запущено!");
        logger.info("Открываю главную страницу...");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/main.jsp");
        requestDispatcher.forward(request, response);
        logger.info("Открыл главную страницу!");
    }
}
