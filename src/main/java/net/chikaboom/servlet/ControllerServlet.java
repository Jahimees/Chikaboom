package net.chikaboom.servlet;

import net.chikaboom.util.*;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
        QueryBuilder builder = new QueryBuilder();
        SqlWhereEntity sqlWhereEntity = new SqlWhereEntity();

        Map<String, SqlComparatorType> fields = new LinkedHashMap<>();
        fields.put("field1", SqlComparatorType.EQUAL);
        fields.put("field2", SqlComparatorType.IS_NOT_NULL);
        fields.put("field3", SqlComparatorType.IS_NULL);
        sqlWhereEntity.setFields(fields);
        Queue<SqlOperandType> queue = new LinkedList<>();
        queue.add(SqlOperandType.AND);
        queue.add(SqlOperandType.OR);
        sqlWhereEntity.setOperands(queue);

        String result = builder.delete().from("table1").where(sqlWhereEntity)
                .join(SqlJoinType.INNER_JOIN, "table1", "table2", "id1", "id2").build();

        logger.info(result);

        builder.clear();

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("feidl1");
        fieldNames.add("feidl2");
        fieldNames.add("feidl3");

        queue.add(SqlOperandType.AND);
        queue.add(SqlOperandType.OR);
        result = builder.insert("table1", fieldNames).where(sqlWhereEntity).build();

        logger.info(result);

        logger.info("Приложение запущено!");
        logger.info("Открываю главную страницу...");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/main.jsp");
        requestDispatcher.forward(request, response);
        logger.info("Открыл главную страницу!");
    }
}
