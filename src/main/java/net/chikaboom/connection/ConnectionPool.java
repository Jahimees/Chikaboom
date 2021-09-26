package net.chikaboom.connection;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static net.chikaboom.constant.LoggerMessageConstant.*;

/**
 * Стандартный пул соединений
 */
public class ConnectionPool {
    private static ConnectionPool instance = null;
    private static final Logger logger = Logger.getLogger(ConnectionPool.class);

    private ConnectionPool() {}

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
            logger.info(CONNECTION_POOL_CREATED);
        }

        return instance;
    }

    public Connection getConnection() {
        logger.info(GETTING_CONNECTION);
        Context ctx;
        Connection connection = null;
        try {
            ctx = new InitialContext();

            DataSource dataSource = (DataSource)ctx.lookup("java:comp/env/jdbc/mainPool");
            connection = dataSource.getConnection();
        } catch (SQLException | NamingException e) {
            logger.error(ERROR_GETTING_CONNECTION, e);
        }

        logger.info(CONNECTION_GOT);
        return connection;
    }
}
